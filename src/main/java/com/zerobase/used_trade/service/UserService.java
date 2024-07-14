package com.zerobase.used_trade.service;

import com.zerobase.used_trade.data.dto.UserDto.Principle;
import com.zerobase.used_trade.data.dto.UserDto.SignInRequest;
import com.zerobase.used_trade.data.dto.UserDto.SignInResponse;
import com.zerobase.used_trade.data.dto.UserDto.SignUpRequest;
import com.zerobase.used_trade.data.dto.UserDto.UpdateRequest;

public interface UserService {
  //회원가입
  Principle signUp(SignUpRequest request);

  SignInResponse signIn(SignInRequest request);

  void updateUserInfo(Long userId, UpdateRequest request);
}
