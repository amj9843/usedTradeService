package com.zerobase.used_trade.service.impl;

import static com.zerobase.used_trade.util.CountUtility.REPORT_MAX_IMG;
import static com.zerobase.used_trade.util.FileUtility.REPORT_FOLDER;

import com.zerobase.used_trade.component.ImageUploader;
import com.zerobase.used_trade.component.PageConverter;
import com.zerobase.used_trade.data.constant.ImageUsing;
import com.zerobase.used_trade.data.constant.ReportSortType;
import com.zerobase.used_trade.data.constant.ReportStatus;
import com.zerobase.used_trade.data.constant.ReportStatusFilterType;
import com.zerobase.used_trade.data.constant.ReportTypeFilterType;
import com.zerobase.used_trade.data.constant.UserRole;
import com.zerobase.used_trade.data.domain.Image;
import com.zerobase.used_trade.data.domain.Report;
import com.zerobase.used_trade.data.domain.User;
import com.zerobase.used_trade.data.dto.ImageDto;
import com.zerobase.used_trade.data.dto.ImageDto.ImageInfo;
import com.zerobase.used_trade.data.dto.ReportDto.AnswerRequest;
import com.zerobase.used_trade.data.dto.ReportDto.EnrollRequest;
import com.zerobase.used_trade.data.dto.ReportDto.Principle;
import com.zerobase.used_trade.data.dto.ReportDto.SimpleInfoResponse;
import com.zerobase.used_trade.exception.impl.AlreadyCompletedException;
import com.zerobase.used_trade.exception.impl.CannotReportSelfException;
import com.zerobase.used_trade.exception.impl.CannotUploadImagesOverTheMaxCountException;
import com.zerobase.used_trade.exception.impl.NoAuthorizeException;
import com.zerobase.used_trade.exception.impl.NoReportException;
import com.zerobase.used_trade.exception.impl.NoUserException;
import com.zerobase.used_trade.repository.ImageRepository;
import com.zerobase.used_trade.repository.ReportRepository;
import com.zerobase.used_trade.repository.UserRepository;
import com.zerobase.used_trade.service.ReportService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {
  private final UserRepository userRepository;
  private final ReportRepository reportRepository;
  private final ImageRepository imageRepository;
  private final ImageUploader imageUploader;

  @Override
  @Transactional
  public Principle enrollReport(Long userId, List<MultipartFile> images, EnrollRequest request) {
    User user = this.userRepository.findById(userId).orElseThrow(NoUserException::new);
    //관리자는 실행 불가
    if (user.getRole() == UserRole.ADMIN) {
      throw new NoAuthorizeException();
    }

    if (request.getReportedId() != null && userId.equals(request.getReportedId())) {
      throw new CannotReportSelfException();
    }

    if (request.getReportedId() != null) {
      //신고할 사용자가 있는 경우 존재하는 회원인지 검색
      this.userRepository.findById(request.getReportedId()).orElseThrow(NoUserException::new);
    }

    if (images != null && images.size() > REPORT_MAX_IMG) {
      throw new CannotUploadImagesOverTheMaxCountException();
    }

    //신고/건의 등록하기
    Report report = this.reportRepository.save(request.toEntity(userId));

    //업로드된 이미지 등록하기
    List<ImageDto.Principle> uploadImages = getUploadImageList(images, report.getId());

    return Principle.fromEntity(report, uploadImages);
  }

  @Override
  @Transactional
  public void enrollAnswer(Long reportId, Long userId, AnswerRequest request) {
    User user = this.userRepository.findById(userId).orElseThrow(NoUserException::new);
    //관리인만 실행 가능
    if (user.getRole() != UserRole.ADMIN) {
      throw new NoAuthorizeException();
    }

    Report report = this.reportRepository.findById(reportId).orElseThrow(NoReportException::new);
    if (report.getStatus() == ReportStatus.COMPLETED) {
      //이미 처리완료된 항목일 경우 답변 등록 불가
      throw new AlreadyCompletedException();
    }

    report.updateAnswer(request);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<SimpleInfoResponse> getReportList(Long userId, int page, int size, ReportTypeFilterType typeFilter,
      ReportStatusFilterType statusFilter, ReportSortType sort) {
    User user = this.userRepository.findById(userId).orElseThrow(NoUserException::new);

    List<SimpleInfoResponse> simpleReportList = this.reportRepository.findAllOfSimple(
        (user.getRole() == UserRole.ADMIN) ? null: userId, typeFilter, statusFilter);

    return new PageConverter<SimpleInfoResponse>().ListToPage(simpleReportList,
        sort.comparator(), PageRequest.of(page, size));
  }

  @Override
  @Transactional(readOnly = true)
  public Principle getReportDetail(Long userId, Long reportId) {
    User user = this.userRepository.findById(userId).orElseThrow(NoUserException::new);

    Report report = this.reportRepository.findById(reportId).orElseThrow(NoReportException::new);
    if (user.getRole() != UserRole.ADMIN && !Objects.equals(report.getReporterId(), userId)) {
      throw new NoAuthorizeException();
    }

    List<ImageDto.Principle> images =
        this.imageRepository.findAllByBoardIdAndUsed(reportId, ImageUsing.REPORT)
            .stream().map(ImageDto.Principle::fromEntity).toList();

    return Principle.fromEntity(report, images);
  }

  @Transactional
  public List<ImageDto.Principle> getUploadImageList(List<MultipartFile> images, Long reportId) {
    List<ImageDto.Principle> uploadImages = new ArrayList<>();

    imageUploader.uploadImageList(images, REPORT_FOLDER).forEach(url-> {
      try {
        Image image = this.imageRepository.save(ImageInfo.toEntity(reportId, ImageUsing.REPORT, url));

        uploadImages.add(ImageDto.Principle.fromEntity(image));
      } catch (Exception e){
        log.info("cannot record image about report to DB -> url: {}, reportId: {} ", url, reportId);

        //DB 저장 실패 시 S3 bucket 에서 이미지 삭제
        imageUploader.deleteImageByUrl(url);
      }
    });

    return uploadImages;
  }
}
