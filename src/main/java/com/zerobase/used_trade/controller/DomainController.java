package com.zerobase.used_trade.controller;

import com.zerobase.used_trade.annotation.ValidEnum;
import com.zerobase.used_trade.data.constant.DomainFilterType;
import com.zerobase.used_trade.data.constant.DomainSortType;
import com.zerobase.used_trade.data.constant.SubscribeType;
import com.zerobase.used_trade.data.constant.SuccessCode;
import com.zerobase.used_trade.data.constant.EmployeeSortType;
import com.zerobase.used_trade.data.dto.DomainDto;
import com.zerobase.used_trade.data.dto.ResultDto;
import com.zerobase.used_trade.service.DomainService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/domain")
public class DomainController {
  private final DomainService domainService;

  @Operation(summary = "관리자 도메인 등록")
  @PostMapping("/enroll")
  public ResponseEntity<?> enrollDomain(@Validated @RequestBody DomainDto.EnrollRequest request) {
    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.CREATED_SUCCESS.status(), SuccessCode.CREATED_SUCCESS.message(),
        this.domainService.enrollDomain(request))
    );
  }

  @Operation(summary = "등록된 관리자 도메인 목록 조회")
  @GetMapping
  public ResponseEntity<?> getDomainList(
      @RequestParam(name = "companyName", required = false) String searchCompanyName,
      @RequestParam(name = "domain", required = false) String searchDomain,
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size,
      @RequestParam(name = "criteria", required = false, defaultValue = "NAMEASC")
      @ValidEnum(enumClass = DomainSortType.class) String criteria,
      @RequestParam(name = "filter", required = false, defaultValue = "ALL")
      @ValidEnum(enumClass = DomainFilterType.class) String filter
  ) {
    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.READ_SUCCESS.status(),
            SuccessCode.READ_SUCCESS.message(),
            this.domainService.getDomainList(searchCompanyName, searchDomain,
                page, size, DomainSortType.valueOf(criteria),
                DomainFilterType.valueOf(filter))));
  }

  @Operation(summary = "관리자 도메인 상세정보 확인")
  @GetMapping("/detail/{domainId}")
  public ResponseEntity<?> getDomainDetail(@PathVariable("domainId") Long domainId,
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size,
      @RequestParam(name = "criteria", required = false, defaultValue = "NAMEASC")
      @ValidEnum(enumClass = EmployeeSortType.class) String criteria
  ) {
    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.READ_SUCCESS.status(), SuccessCode.READ_SUCCESS.message(),
            this.domainService.getDomainDetail(domainId, page, size, EmployeeSortType.valueOf(criteria)))
    );
  }

  @Operation(summary = "관리자 도메인 갱신")
  @PatchMapping("/extension/{domainId}")
  public ResponseEntity<?> extendPeriod(@PathVariable("domainId") Long domainId,
      @Validated @RequestBody DomainDto.ExtensionRequest request) {
    this.domainService.extendPeriodOfDomain(domainId, SubscribeType.valueOf(request.getPeriod()));

    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.EXTEND_PERIOD_SUCCESS.status(), SuccessCode.EXTEND_PERIOD_SUCCESS.message())
    );
  }

  @Operation(summary = "도메인 정보 수정")
  @PatchMapping("/update/{domainId}")
  public ResponseEntity<?> updateDomain(@PathVariable("domainId") Long domainId,
      @Validated @RequestBody DomainDto.UpdateRequest request) {
    this.domainService.updateDomainInfo(domainId, request);

    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.UPDATED_SUCCESS.status(), SuccessCode.UPDATED_SUCCESS.message())
    );
  }

  @Operation(summary = "도메인 정보 삭제")
  @DeleteMapping("/delete/{domainId}")
  public ResponseEntity<?> deleteDomain(@PathVariable("domainId") Long domainId) {
    this.domainService.deleteDomain(domainId);

    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.DELETED_SUCCESS.status(), SuccessCode.DELETED_SUCCESS.message())
    );
  }
}
