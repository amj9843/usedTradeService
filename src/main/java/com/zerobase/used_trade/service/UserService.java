package com.zerobase.used_trade.service;

import com.zerobase.used_trade.data.dto.UserDto.Principle;
import com.zerobase.used_trade.data.dto.UserDto.SignUpRequest;

public interface UserService {
  //회원가입
  Principle signUp(SignUpRequest request);

  /* TODO 로그인
  SignInResponse signIn(SignInRequest request);
   */

  /* TODO 정보 변경
  void updateUserInfo(Long userId, Long signInUserId, UpdateRequest request);
   */
}
