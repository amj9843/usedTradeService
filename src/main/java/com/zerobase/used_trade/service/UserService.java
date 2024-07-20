package com.zerobase.used_trade.service;

import com.zerobase.used_trade.data.constant.UserRole;
import com.zerobase.used_trade.data.domain.User;
import com.zerobase.used_trade.data.dto.UserDto.Principle;
import com.zerobase.used_trade.data.dto.UserDto.SignInRequest;
import com.zerobase.used_trade.data.dto.UserDto.SignInResponse;
import com.zerobase.used_trade.data.dto.UserDto.SignUpRequest;
import com.zerobase.used_trade.data.dto.UserDto.UpdateRequest;

public interface UserService {
  Principle findUserById(Long userId);

  //회원가입
  Principle signUp(SignUpRequest request);

  SignInResponse signIn(SignInRequest request);

  void updateUserInfo(Long userId, UpdateRequest request);

  void updateUserRole(Long userId, UserRole nowRole, UserRole changeRole);
}
