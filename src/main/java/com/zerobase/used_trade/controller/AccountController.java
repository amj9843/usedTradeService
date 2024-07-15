package com.zerobase.used_trade.controller;

import com.zerobase.used_trade.data.constant.SuccessCode;
import com.zerobase.used_trade.data.dto.AccountDto;
import com.zerobase.used_trade.data.dto.ResultDto;
import com.zerobase.used_trade.service.AccountService;
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
@RequestMapping("/account")
public class AccountController {
  private final AccountService accountService;
  //TODO JWT 사용 이후는 @PreAuthorize("hasAnyRole('USER', 'DENIED')") 로 관리자는 이용 못하게 하기(조회 제외)

  //계좌 등록
  @Operation(summary = "계좌 등록")
  @PostMapping
  public ResponseEntity<?> enrollAccount(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @Validated @RequestBody AccountDto.EnrollRequest request) {
    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.CREATED_SUCCESS.status(), SuccessCode.CREATED_SUCCESS.message(),
            this.accountService.enrollAccount(userId, request))
    );
  }

  //계좌 조회
  @Operation(summary = "계좌 조회")
  @GetMapping
  public ResponseEntity<?> getAccountList(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.READ_SUCCESS.status(),
            SuccessCode.READ_SUCCESS.message(),
            this.accountService.getAccountList(userId, page, size)));
  }

  //계좌 수정
  @Operation(summary = "계좌 수정")
  @PatchMapping("/{accountId}")
  public ResponseEntity<?> updateAccount(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @PathVariable("accountId") Long accountId,
      @Validated @RequestBody AccountDto.UpdateRequest request
  ) {
    this.accountService.updateAccountInfo(userId, accountId, request);

    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.UPDATED_SUCCESS.status(), SuccessCode.UPDATED_SUCCESS.message())
    );
  }

  //계좌 삭제
  @Operation(summary = "계좌 삭제")
  @DeleteMapping("/{accountId}")
  public ResponseEntity<?> deleteAccount(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @PathVariable("accountId") Long accountId
  ) {
    this.accountService.deleteAccount(userId, accountId);

    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.DELETED_SUCCESS.status(), SuccessCode.DELETED_SUCCESS.message())
    );
  }
}
