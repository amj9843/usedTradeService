package com.zerobase.used_trade.data.domain;

import com.zerobase.used_trade.data.constant.UserRole;
import com.zerobase.used_trade.data.dto.UserDto.UpdateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "user")
@Entity
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;

  @Column(name = "domain_id")
  private Long domainId;

  @Column(name = "email")
  private String email;

  @Column(name = "password")
  private String password;

  @Column(name = "name")
  private String name;

  @Column(name = "nick_name")
  private String nickName;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "role")
  @Enumerated(EnumType.STRING)
  private UserRole role;

  public void update(
      UpdateRequest request) {
    if (request.getPassword() != null && !request.getPassword().isBlank()) {
      this.password = request.getPassword();
    }
    if (request.getName() != null && !request.getName().isBlank()) {
      this.name = request.getName().trim();
    }
    if (request.getNickName() != null && !request.getNickName().isBlank()) {
      this.nickName = request.getNickName().trim();
    }
    if (request.getPhoneNumber() != null) {
      this.phoneNumber = request.getPhoneNumber();
    }
  }

  @Builder
  public User(Long domainId, String email, String password,
      String name, String nickName, String phoneNumber, UserRole role) {
    this.domainId = domainId;
    this.email = email;
    this.password = password;
    this.name = name;
    this.nickName = nickName;
    this.phoneNumber = phoneNumber;
    this.role = role;
  }
}
