package com.zerobase.used_trade.controller;

import com.zerobase.used_trade.data.constant.SuccessCode;
import com.zerobase.used_trade.data.dto.DealMethodDto.EnrollRequest;
import com.zerobase.used_trade.data.dto.DealMethodDto.ProductDetailMethod;
import com.zerobase.used_trade.data.dto.ResultDto;
import com.zerobase.used_trade.service.DealMethodService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/deal-method")
public class DealMethodController {
  private final DealMethodService dealMethodService;

  //거래 방식 등록
  @Operation(summary = "거래 방식 등록")
  @PostMapping
  public ResponseEntity<?> enrollReport(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @Validated @RequestBody EnrollRequest request) {
    ProductDetailMethod response = this.dealMethodService.enrollDealMethod(userId, request);

    SuccessCode successCode = SuccessCode.CREATED_SUCCESS;
    if (request.getParcel() != null && request.getParcel().isFlag() && response.getParcel() == null) {
      successCode = SuccessCode.PARTIAL_SUCCESS;
    }
    if (request.getConvenience() != null && request.getConvenience().isFlag() && response.getConvenience() == null) {
      successCode = SuccessCode.PARTIAL_SUCCESS;
    }
    if (request.getMeetings().size() > response.getMeeting().size()) {
      successCode = SuccessCode.PARTIAL_SUCCESS;
    }

    return ResponseEntity.ok(
        ResultDto.res(successCode.status(), successCode.message(), response)
    );
  }

  //상품별 거래 방식 조회
  @Operation(summary = "상품별 거래 방식 조회")
  @GetMapping("/by-product/{productId}")
  public ResponseEntity<?> getDealMethodsByProduct(
      @PathVariable("productId") Long productId) {
    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.READ_SUCCESS.status(), SuccessCode.READ_SUCCESS.message(),
            this.dealMethodService.getDealMethodsByProduct(productId))
    );
  }

  //거래 방식 삭제
  @Operation(summary = "거래 방식 삭제")
  @DeleteMapping("/{dealMethodId}")
  public ResponseEntity<?> deleteImagesOnBoard(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @PathVariable("dealMethodId") Long dealMethodId) {
    this.dealMethodService.deleteDealMethod(userId, dealMethodId);

    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.DELETED_SUCCESS.status(), SuccessCode.DELETED_SUCCESS.message())
    );
  }
}
