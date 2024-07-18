package com.zerobase.used_trade.service.impl;

import com.zerobase.used_trade.component.ImageUploader;
import com.zerobase.used_trade.data.constant.ImageUsing;
import com.zerobase.used_trade.data.constant.UserRole;
import com.zerobase.used_trade.data.domain.Image;
import com.zerobase.used_trade.data.domain.User;
import com.zerobase.used_trade.data.dto.ImageDto;
import com.zerobase.used_trade.data.dto.ImageDto.ImageInfo;
import com.zerobase.used_trade.data.dto.ImageDto.Principle;
import com.zerobase.used_trade.exception.impl.CannotUploadImagesOverTheMaxCountException;
import com.zerobase.used_trade.exception.impl.CannotUploadOnlyImagesOfReportException;
import com.zerobase.used_trade.exception.impl.NoAuthorizeException;
import com.zerobase.used_trade.exception.impl.NoUserException;
import com.zerobase.used_trade.repository.ImageRepository;
import com.zerobase.used_trade.repository.ProductRepository;
import com.zerobase.used_trade.repository.ReviewRepository;
import com.zerobase.used_trade.repository.UserRepository;
import com.zerobase.used_trade.service.ImageService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {
  private final ImageRepository imageRepository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;
  private final ReviewRepository reviewRepository;
  private final ImageUploader imageUploader;

  @Override
  @Transactional
  public List<Principle> enrollImageOnBoard(Long userId, List<MultipartFile> images, ImageInfo request) {
    User user = this.userRepository.findById(userId).orElseThrow(NoUserException::new);

    //이용 게시판 확인
    ImageUsing type = ImageUsing.valueOf(request.getUsed());

    if (type == ImageUsing.PRODUCT && user.getRole() == UserRole.ADMIN) {
      if (!this.productRepository.canUpdatedProductByAdmin(userId, request.getBoardId())) {
        throw new NoAuthorizeException();
      }
    } else if (type == ImageUsing.PRODUCT) {
      if (!this.productRepository.canUpdatedProductByUser(userId, request.getBoardId())) {
        throw new NoAuthorizeException();
      }
    } else if (type == ImageUsing.REVIEW) {
      if (!this.reviewRepository.existsByIdAndBuyerId(request.getBoardId(), userId)) {
        throw new NoAuthorizeException();
      }
    } else if (type == ImageUsing.REPORT) {
      throw new CannotUploadOnlyImagesOfReportException();
    }

    //기존 등록된 이미지 장 수와 등록하려는 이미지 장 수의 합이 최대치를 넘으면 등록 불가
    if (this.imageRepository.countByBoardIdAndUsed(request.getBoardId(), type) + images.size() > type.maxCount()) {
      throw new CannotUploadImagesOverTheMaxCountException();
    }

    return getUploadImageList(images, type, request.getBoardId(), type.folder());
  }

  @Override
  @Transactional
  public List<Principle> deleteImagesOnBoard(Long userId, Set<Long> imageIdList) {
    User user = this.userRepository.findById(userId).orElseThrow(NoUserException::new);

    List<Principle> deletedImages = new ArrayList<>();

    for (Long imageId: imageIdList) {
      Optional<Image> image = this.imageRepository.findById(imageId);

      if (image.isEmpty()) {
        log.error("cannot delete image cause there's no image which imageId is insert id.-> imageId: {} ", imageId);
      } else {
        Image cur = image.get();

        //권한 확인
        if (cur.getUsed() == ImageUsing.REPORT) {
          //신고/건의일 경우 이미지 삭제 불가(작업 무시)
          log.error("cannot delete image which is enrolled on report.-> imageId: {} ", imageId);

          continue;
        } else if (cur.getUsed() == ImageUsing.PRODUCT && user.getRole() == UserRole.ADMIN) {
          //상품 이미지이고 이미지를 삭제하려는 이용자가 관리인인 경우
          if (!this.productRepository.canUpdatedProductByAdmin(userId, cur.getBoardId())) {
            log.error("cannot delete image cause no authorization for user.-> imageId: {} ", imageId);

            continue;
          }
        } else if (cur.getUsed() == ImageUsing.PRODUCT) {
          //상품 이미지이고 이미지를 삭제하려는 이용자가 관리인이 아닌 경우
          if (!this.productRepository.canUpdatedProductByUser(userId, cur.getBoardId())) {
            log.error("cannot delete image cause no authorization for user.-> imageId: {} ", imageId);

            continue;
          }
        } else if (cur.getUsed() == ImageUsing.REVIEW) {
          //리뷰 이미지이고 이미지를 삭제하려는 이용자가 본인이 올렸던 이미지인 경우
          if (!this.reviewRepository.existsByIdAndBuyerId(cur.getBoardId(), userId)) {
            log.error("cannot delete image cause no authorization for user.-> imageId: {} ", imageId);

            continue;
          }
        }

        try {
          imageUploader.deleteImageByUrl(cur.getSrc());

          this.imageRepository.delete(cur);
          deletedImages.add(Principle.fromEntity(cur));
        } catch (Exception e) {
          log.error("failed to delete image. -> imageUrl: {} ", cur.getSrc());
        }
      }
    }

    return deletedImages;
  }

  @Transactional
  public List<ImageDto.Principle> getUploadImageList(List<MultipartFile> images,
      ImageUsing used, Long boardId, String folder) {
    List<ImageDto.Principle> uploadImages = new ArrayList<>();

    imageUploader.uploadImageList(images, folder).forEach(url-> {
      try {
        Image image = this.imageRepository.save(ImageInfo.toEntity(boardId, used, url));

        uploadImages.add(ImageDto.Principle.fromEntity(image));
      } catch (Exception e){
        log.info("cannot record image about {} to DB -> url: {}, productId: {} ", folder, url, boardId);

        imageUploader.deleteImageByUrl(url);
      }
    });

    return uploadImages;
  }
}
