package com.zerobase.used_trade.controller;

import com.zerobase.used_trade.data.constant.SuccessCode;
import com.zerobase.used_trade.data.constant.UserRole;
import com.zerobase.used_trade.data.domain.User;
import com.zerobase.used_trade.data.dto.ResultDto;
import com.zerobase.used_trade.data.dto.UserDto;
import com.zerobase.used_trade.exception.impl.NoAuthorizeException;
import com.zerobase.used_trade.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
  private final UserService userService;

  @Operation(summary = "회원 가입")
  @PostMapping("/sign-up")
  public ResponseEntity<?> signUp(@Validated @RequestBody UserDto.SignUpRequest request) {
    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.CREATED_SUCCESS.status(), SuccessCode.CREATED_SUCCESS.message(),
            this.userService.signUp(request))
    );
  }

  @Operation(summary = "로그인")
  @PostMapping("/sign-in")
  public ResponseEntity<?> signIn(@Validated @RequestBody UserDto.SignInRequest request) {
    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.SIGN_IN_SUCCESS.status(), SuccessCode.SIGN_IN_SUCCESS.message(),
            this.userService.signIn(request))
    );
  }

  @Operation(summary = "사용자(로그인한 사용자) 정보 변경")
  @PatchMapping
  public ResponseEntity<?> updateUserInfo(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @Validated @RequestBody UserDto.UpdateRequest request) {
    this.userService.updateUserInfo(userId, request);

    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.UPDATED_SUCCESS.status(), SuccessCode.UPDATED_SUCCESS.message())
    );
  }

  @Operation(summary = "관리자의 사용자 권한 변경(일반 사용자 > 거래 불가자)")
  @PatchMapping("/denied/{userId}")
  public ResponseEntity<?> updateUserRoleToDenied(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long adminId,
      @PathVariable("userId") Long userId) {
    User user = this.userService.findUserById(adminId);
    if (user.getRole() != UserRole.ADMIN) {
      throw new NoAuthorizeException();
    }

    this.userService.updateUserRole(userId, UserRole.USER, UserRole.DENIED);

    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.UPDATED_SUCCESS.status(), SuccessCode.UPDATED_SUCCESS.message())
    );
  }

  @Operation(summary = "관리자의 사용자 권한 변경(거래 불가자 > 일반 사용자)")
  @PatchMapping("/common/{userId}")
  public ResponseEntity<?> updateUserRoleToUser(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long adminId,
      @PathVariable("userId") Long userId) {
    User user = this.userService.findUserById(adminId);
    if (user.getRole() != UserRole.ADMIN) {
      throw new NoAuthorizeException();
    }

    this.userService.updateUserRole(userId, UserRole.DENIED, UserRole.USER);

    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.UPDATED_SUCCESS.status(), SuccessCode.UPDATED_SUCCESS.message())
    );
  }

  /*
  TODO 회원 탈퇴
  @Operation(summary = "회원 탈퇴")
  @DeleteMapping
   */

  /*
  TODO 마이페이지 조회
  @Operation(summary = "마이페이지 조회")
  @GetMapping("/detail/private")
   */

  /*
  TODO 사용자 정보 조회
  @Operation(summary = "사용자 정보 조회")
  @GetMapping("/detail/public/{userId}")
   */
}
