package com.zerobase.used_trade.controller;

import com.zerobase.used_trade.data.constant.SuccessCode;
import com.zerobase.used_trade.data.dto.ProductDto.EnrollConsignmentRequest;
import com.zerobase.used_trade.data.dto.ProductDto.EnrollConsignmentResponse;
import com.zerobase.used_trade.data.dto.ProductDto.EnrollDirectRequest;
import com.zerobase.used_trade.data.dto.ProductDto.Principle;
import com.zerobase.used_trade.data.dto.ResultDto;
import com.zerobase.used_trade.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/product")
public class ProductController {
  private final ProductService productService;

  //상품 등록(직접 판매)
  @Operation(summary = "사용자가 직접 판매할 상품 등록")
  @PostMapping(value = "/direct",
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<?> enrollProductDirect(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @RequestPart(value = "files", required = false) List<MultipartFile> images,
      @Validated @RequestPart(value = "request") EnrollDirectRequest request) {
    Principle response = this.productService.enrollProductDirect(userId, images, request);

    SuccessCode successCode = (images != null && response.getImages().size() < images.size()) ?
        SuccessCode.PARTIAL_SUCCESS : SuccessCode.CREATED_SUCCESS;

    return ResponseEntity.ok(
        ResultDto.res(successCode.status(), successCode.message(), response)
    );
  }

  //상품 등록(위탁 신청)
  @Operation(summary = "사용자가 위탁 신청할 상품 등록")
  @PostMapping(value = "/consignment",
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<?> enrollProductConsignment(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @RequestPart(value = "files", required = false) List<MultipartFile> images,
      @Validated @RequestPart(value = "request") EnrollConsignmentRequest request) {
    EnrollConsignmentResponse response = this.productService.enrollProductConsignment(userId, images, request);

    SuccessCode successCode = (images != null && response.getProductInfo().getImages().size() < images.size()) ?
        SuccessCode.PARTIAL_SUCCESS : SuccessCode.CREATED_SUCCESS;

    return ResponseEntity.ok(
        ResultDto.res(successCode.status(), successCode.message(), response)
    );
  }
}
