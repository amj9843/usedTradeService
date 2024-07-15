package com.zerobase.used_trade.data.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.zerobase.used_trade.annotation.EmptyOrNotBlank;
import com.zerobase.used_trade.annotation.Password;
import com.zerobase.used_trade.annotation.PhoneNumber;
import com.zerobase.used_trade.annotation.ShortString;
import com.zerobase.used_trade.data.constant.UserRole;
import com.zerobase.used_trade.data.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserDto {
  @Data
  @Builder
  public static class Principle {
    private Long id;
    private Long domainId;
    private String email;
    private String name;
    private String nickName;
    private String phoneNumber;
    private UserRole role;

    public static Principle fromEntity(User user) {
      return Principle.builder()
          .id(user.getId())
          .domainId(user.getDomainId())
          .email(user.getEmail())
          .name(user.getName())
          .nickName(user.getNickName())
          .phoneNumber(user.getPhoneNumber())
          .role(user.getRole())
          .build();
    }
  }
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class SignUpRequest {
    @NotEmpty(message = "{validation.User.email.NotEmpty}")
    @Email(message = "{validation.Pattern.Email}")
    private String email;

    @NotEmpty(message = "{validation.User.password.NotEmpty}")
    @Password
    private String password;

    @NotBlank(message = "{validation.User.name.NotBlank}")
    @ShortString
    private String name;

    @NotBlank(message = "{validation.User.nickName.NotBlank}")
    @ShortString
    private String nickName;

    @PhoneNumber
    private String phoneNumber;

    public User toEntity(Long domainId) {
      return User.builder()
          .domainId(domainId)
          .email(this.email)
          .password(this.password)
          .name(this.name.trim())
          .nickName(this.nickName.trim())
          .phoneNumber(this.phoneNumber)
          .role((domainId == null || domainId == 0L) ?
              UserRole.USER : UserRole.ADMIN)
          .build();
    }
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class SignInRequest {
    @NotEmpty(message = "{validation.User.email.NotEmpty}")
    @Email(message = "{validation.Pattern.Email}")
    private String email;

    @NotEmpty(message = "{validation.User.password.NotEmpty}")
    private String password;
  }

  @Data
  @AllArgsConstructor
  public static class SignInResponse {
    private Long id;
    /*TODO 토큰 구현하면 response token 으로 변경
    private String token;
     */
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UpdateRequest{
    @Password
    private String password;

    private String passwordConfirm;

    @EmptyOrNotBlank
    @ShortString
    private String name;

    @EmptyOrNotBlank
    @ShortString
    private String nickName;

    @PhoneNumber
    private String phoneNumber;
  }

  @Data
  public static class Employee{
    private Long id;
    private String email;
    private String name;
    private String nickName;
    private String phoneNumber;
    private int sellScore;

    @QueryProjection
    public Employee(Long id, String email, String name, String nickName, String phoneNumber, int sellScore) {
      this.id = id;
      this.email = email;
      this.name = name;
      this.nickName = nickName;
      this.phoneNumber = phoneNumber;
      this.sellScore = sellScore;
    }
  }
}
