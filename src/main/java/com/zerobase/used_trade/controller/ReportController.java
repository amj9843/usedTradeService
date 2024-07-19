package com.zerobase.used_trade.controller;

import com.zerobase.used_trade.annotation.ValidEnum;
import com.zerobase.used_trade.data.constant.ReportSortType;
import com.zerobase.used_trade.data.constant.ReportStatusFilterType;
import com.zerobase.used_trade.data.constant.ReportTypeFilterType;
import com.zerobase.used_trade.data.constant.SuccessCode;
import com.zerobase.used_trade.data.constant.UserRole;
import com.zerobase.used_trade.data.domain.User;
import com.zerobase.used_trade.data.dto.ReportDto.AnswerRequest;
import com.zerobase.used_trade.data.dto.ReportDto.EnrollRequest;
import com.zerobase.used_trade.data.dto.ReportDto.Principle;
import com.zerobase.used_trade.data.dto.ResultDto;
import com.zerobase.used_trade.exception.impl.NoAuthorizeException;
import com.zerobase.used_trade.service.ReportService;
import com.zerobase.used_trade.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/report")
public class ReportController {
  private final ReportService reportService;
  private final UserService userService;

  //신고/건의 등록
  @Operation(summary = "신고/건의 등록")
  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<?> enrollReport(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @RequestPart(value = "files", required = false) List<MultipartFile> images,
      @Validated @RequestPart(value = "request") EnrollRequest request) {
    Principle response = this.reportService.enrollReport(userId, images, request);

    SuccessCode successCode = (images != null && response.getImages().size() < images.size()) ?
        SuccessCode.PARTIAL_SUCCESS : SuccessCode.CREATED_SUCCESS;

    return ResponseEntity.ok(
        ResultDto.res(successCode.status(), successCode.message(), response)
    );
  }

  //관리자의 답변 등록
  @Operation(summary = "관리자의 답변 등록")
  @PatchMapping("/{reportId}")
  public ResponseEntity<?> enrollAnswer(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @PathVariable("reportId") Long reportId,
      @Validated @RequestBody AnswerRequest request) {
    User user = this.userService.findUserById(userId);
    if (user.getRole() != UserRole.ADMIN) {
      throw new NoAuthorizeException();
    }

    this.reportService.enrollAnswer(reportId, request);

    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.ANSWERED_SUCCESS.status(), SuccessCode.ANSWERED_SUCCESS.message())
    );
  }

  //전체 조회
  @Operation(summary = "등록된 신고/건의 전체 조회(사용자의 경우 본인이 등록한 신고/건의 전체 조회)")
  @GetMapping
  public ResponseEntity<?> getReportList(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size,
      @RequestParam(name = "typeFilter", required = false, defaultValue = "ALL")
      @ValidEnum(enumClass = ReportTypeFilterType.class) String typeFilter,
      @RequestParam(name = "statusFilter", required = false, defaultValue = "ALL")
      @ValidEnum(enumClass = ReportStatusFilterType.class) String statusFilter,
      @RequestParam(name = "sort", required = false, defaultValue = "CREATEDATDESC")
      @ValidEnum(enumClass = ReportSortType.class) String sort
  ) {
    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.READ_SUCCESS.status(),
            SuccessCode.READ_SUCCESS.message(),
            this.reportService.getReportList(userId, page, size,
                ReportTypeFilterType.valueOf(typeFilter), ReportStatusFilterType.valueOf(statusFilter),
                ReportSortType.valueOf(sort))));
  }

  //개별 조회
  @Operation(summary = "신고/건의 개별 조회")
  @GetMapping("/detail/{reportId}")
  public ResponseEntity<?> getReportDetail(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @PathVariable("reportId") Long reportId) {
    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.READ_SUCCESS.status(), SuccessCode.READ_SUCCESS.message(),
            this.reportService.getReportDetail(userId, reportId))
    );
  }
}
