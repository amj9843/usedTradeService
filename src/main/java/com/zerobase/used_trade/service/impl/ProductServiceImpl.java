package com.zerobase.used_trade.service.impl;

import static com.zerobase.used_trade.util.CountUtility.PRODUCT_MAX_IMG;
import static com.zerobase.used_trade.util.FileUtility.PRODUCT_FOLDER;

import com.zerobase.used_trade.component.ImageUploader;
import com.zerobase.used_trade.data.constant.ImageUsing;
import com.zerobase.used_trade.data.constant.UserRole;
import com.zerobase.used_trade.data.domain.Account;
import com.zerobase.used_trade.data.domain.Address;
import com.zerobase.used_trade.data.domain.Category;
import com.zerobase.used_trade.data.domain.Consignment;
import com.zerobase.used_trade.data.domain.Image;
import com.zerobase.used_trade.data.domain.Product;
import com.zerobase.used_trade.data.domain.User;
import com.zerobase.used_trade.data.dto.CategoryDto;
import com.zerobase.used_trade.data.dto.ImageDto;
import com.zerobase.used_trade.data.dto.ImageDto.ImageInfo;
import com.zerobase.used_trade.data.dto.ProductDto.EnrollConsignmentRequest;
import com.zerobase.used_trade.data.dto.ProductDto.EnrollConsignmentResponse;
import com.zerobase.used_trade.data.dto.ProductDto.EnrollDirectRequest;
import com.zerobase.used_trade.data.dto.ProductDto.Principle;
import com.zerobase.used_trade.data.dto.ProductMatchCategoryDto;
import com.zerobase.used_trade.exception.impl.CannotUploadImagesOverTheMaxCountException;
import com.zerobase.used_trade.exception.impl.CannotUseBeforeEnrollAccountException;
import com.zerobase.used_trade.exception.impl.CannotUseBeforeEnrollAddressException;
import com.zerobase.used_trade.exception.impl.NoAuthorizeException;
import com.zerobase.used_trade.exception.impl.NoMatchAccountAndUserException;
import com.zerobase.used_trade.exception.impl.NoMatchAddressAndUserException;
import com.zerobase.used_trade.exception.impl.NoUserException;
import com.zerobase.used_trade.repository.AccountRepository;
import com.zerobase.used_trade.repository.AddressRepository;
import com.zerobase.used_trade.repository.CategoryRepository;
import com.zerobase.used_trade.repository.ConsignmentRepository;
import com.zerobase.used_trade.repository.ImageRepository;
import com.zerobase.used_trade.repository.ProductMatchCategoryRepository;
import com.zerobase.used_trade.repository.ProductRepository;
import com.zerobase.used_trade.repository.UserRepository;
import com.zerobase.used_trade.service.ProductService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
  private final UserRepository userRepository;
  private final AddressRepository addressRepository;
  private final AccountRepository accountRepository;
  private final ProductRepository productRepository;
  private final ProductMatchCategoryRepository matchRepository;
  private final CategoryRepository categoryRepository;
  private final ConsignmentRepository consignmentRepository;
  private final ImageRepository imageRepository;
  private final ImageUploader imageUploader;

  @Override
  @Transactional
  public Principle enrollProductDirect(Long userId, List<MultipartFile> images, EnrollDirectRequest request) {
    User user = this.userRepository.findById(userId).orElseThrow(NoUserException::new);
    //일반 사용자만 실행 가능
    if (user.getRole() != UserRole.USER) {
      throw new NoAuthorizeException();
    }

    //계좌 정보가 등록되지 않은 사용자는 이용 불가
    if (!this.accountRepository.existsByUserId(userId)) {
      throw new CannotUseBeforeEnrollAccountException();
    }

    if (images != null && images.size() > PRODUCT_MAX_IMG) {
      throw new CannotUploadImagesOverTheMaxCountException();
    }

    //상품 등록하기
    Product product = this.productRepository.save(request.toEntity(userId));

    //업로드된 이미지 등록하기
    List<ImageDto.Principle> uploadImages = getUploadImageList(images, product.getId());

    //카테고리 매칭시키기
    Set<CategoryDto.Principle> categories = getCategorySet(request.getCategory(), product.getId());

    return Principle.fromEntity(product, uploadImages, categories);
  }

  @Override
  @Transactional
  public EnrollConsignmentResponse enrollProductConsignment(Long userId, List<MultipartFile> images,
      EnrollConsignmentRequest request) {
    User user = this.userRepository.findById(userId).orElseThrow(NoUserException::new);
    //일반 사용자만 실행 가능
    if (user.getRole() != UserRole.USER) {
      throw new NoAuthorizeException();
    }
    
    //주소 정보 확인
    Address address = (request.getAddressId() == null || request.getAddressId() == 0L) ?
        this.addressRepository.findByUserIdAndRepresentativeTrue(userId)
            .orElseThrow(CannotUseBeforeEnrollAddressException::new)
        : this.addressRepository.findByIdAndUserId(request.getAddressId(), userId)
            .orElseThrow(NoMatchAddressAndUserException::new);

    //계좌 정보 확인
    Account account = (request.getAccountId() == null || request.getAccountId() == 0L) ?
        this.accountRepository.findByUserIdAndRepresentativeTrue(userId)
            .orElseThrow(CannotUseBeforeEnrollAccountException::new)
        : this.accountRepository.findByIdAndUserId(request.getAccountId(), userId)
            .orElseThrow(NoMatchAccountAndUserException::new);
    
    //이미지 개수 초과 시 등록 불가
    if (images != null && images.size() > PRODUCT_MAX_IMG) {
      throw new CannotUploadImagesOverTheMaxCountException();
    }

    //상품 등록하기
    Product product = this.productRepository.save(request.toEntity(userId));
    Consignment consignment = this.consignmentRepository.save(request.toEntity(product.getId(), address, account));

    //업로드된 이미지 등록하기
    List<ImageDto.Principle> uploadImages = getUploadImageList(images, product.getId());

    //카테고리 매칭시키기
    Set<CategoryDto.Principle> categories = getCategorySet(request.getCategory(), product.getId());

    return EnrollConsignmentResponse.fromEntity(product, consignment, uploadImages, categories);
  }

  @Transactional
  public List<ImageDto.Principle> getUploadImageList(List<MultipartFile> images, Long productId) {
    List<ImageDto.Principle> uploadImages = new ArrayList<>();

    imageUploader.uploadImageList(images, PRODUCT_FOLDER).forEach(url-> {
      try {
        Image image = this.imageRepository.save(ImageInfo.toEntity(productId, ImageUsing.PRODUCT, url));

        uploadImages.add(ImageDto.Principle.fromEntity(image));
      } catch (Exception e){
        log.info("cannot record image about product to DB -> url: {}, productId: {} ", url, productId);
      }
    });

    return uploadImages;
  }

  @Transactional
  public Set<CategoryDto.Principle> getCategorySet(Set<Long> categoryIds, Long productId) {
    Set<CategoryDto.Principle> categories = new HashSet<>();

    categoryIds.forEach(categoryId-> {
      Optional<Category> category = this.categoryRepository.findById(categoryId);
      if (category.isEmpty()) {
        log.info(
            "cannot match product on category cause there's no category by insert id-> productId: {}, categoryId: {} ",
            productId, categoryId);
      } else {
        try {
          //매치테이블에 등록
          this.matchRepository.save(ProductMatchCategoryDto.EnrollRequest.toEntity(productId, categoryId));

          categories.add(CategoryDto.Principle.fromEntity(category.get()));
        } catch(DataIntegrityViolationException e) {
          //이미 등록된 항목일 때
          log.info("already enroll product on category-> productId: {}, categoryId: {} ", productId, categoryId);
        } catch(Exception e) {
          log.info(
              "cannot match product on category-> productId: {}, categoryId: {} ",
              productId, categoryId);
        }
      }
    });

    return categories;
  }
}
