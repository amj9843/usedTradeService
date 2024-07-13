package com.zerobase.used_trade.controller;

import com.zerobase.used_trade.data.constant.SuccessCode;
import com.zerobase.used_trade.data.dto.ResultDto;
import com.zerobase.used_trade.data.dto.UserDto;
import com.zerobase.used_trade.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

  /*
  TODO 로그인
  @Operation(summary = "로그인")
  @PostMapping("/sign-in")
  public ResponseEntity<?> signIn(@Validated @RequestBody UserDto.SignInRequest request) {
    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.CREATED_SUCCESS.status(), SuccessCode.CREATED_SUCCESS.message(),
            this.userService.signIn(request))
    );
  }
   */

  /*
  TODO 회원 정보 변경
  @Operation(summary = "사용자(로그인한 사용자) 정보 변경")
  @PatchMapping("/{userId}")
  public ResponseEntity<?> updateUserInfo(@PathVariable("userId") Long userId,
      //TODO 임시로 RequestParam에 로그인한 userId를 받아옴, token 발급 이후는 @Authentication 에서 받아옴
      @RequestParam(name = "signInUserId") Long signInUserId,
      @Validated @RequestBody UserDto.UpdateRequest request) {
    this.userService.updateUserInfo(userId, signInUserId, request);

    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.UPDATED_SUCCESS.status(), SuccessCode.UPDATED_SUCCESS.message())
    );
  }
   */

  /*
  TODO 회원 권한 변경
  @Operation(summary = "관리자의 사용자 권한 변경(일반 사용자 > 거래 불가자)")
  @PatchMapping("/denied/{userId}")
   */

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
