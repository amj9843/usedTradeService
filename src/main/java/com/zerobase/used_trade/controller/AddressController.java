package com.zerobase.used_trade.controller;

import com.zerobase.used_trade.data.constant.SuccessCode;
import com.zerobase.used_trade.data.dto.AddressDto;
import com.zerobase.used_trade.data.dto.ResultDto;
import com.zerobase.used_trade.service.AddressService;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/address")
public class AddressController {
  private final AddressService addressService;

  //주소 등록
  @Operation(summary = "주소 등록")
  @PostMapping
  public ResponseEntity<?> enrollAddress(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @Validated @RequestBody AddressDto.EnrollRequest request) {
    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.CREATED_SUCCESS.status(), SuccessCode.CREATED_SUCCESS.message(),
            this.addressService.enrollAddress(userId, request))
    );
  }

  //주소 조회
  @Operation(summary = "주소 조회")
  @GetMapping
  public ResponseEntity<?> getAddressList(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.READ_SUCCESS.status(),
            SuccessCode.READ_SUCCESS.message(),
            this.addressService.getAddressList(userId, page, size)));
  }

  //주소 수정
  @Operation(summary = "주소 수정")
  @PatchMapping("/{addressId}")
  public ResponseEntity<?> updateAddress(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @PathVariable("addressId") Long addressId,
      @Validated @RequestBody AddressDto.UpdateRequest request
  ) {
    this.addressService.updateAddressInfo(userId, addressId, request);

    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.UPDATED_SUCCESS.status(), SuccessCode.UPDATED_SUCCESS.message())
    );
  }

  //주소 삭제
  @Operation(summary = "주소 삭제")
  @DeleteMapping("/{addressId}")
  public ResponseEntity<?> deleteAddress(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @PathVariable("addressId") Long addressId
  ) {
    this.addressService.deleteAddress(userId, addressId);

    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.DELETED_SUCCESS.status(), SuccessCode.DELETED_SUCCESS.message())
    );
  }
}
