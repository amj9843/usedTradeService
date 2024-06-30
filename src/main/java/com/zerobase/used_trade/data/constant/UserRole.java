package com.zerobase.used_trade.data.constant;

public enum UserRole {
  USER("일반 사용자"),
  DENIED("거래 불가자"),
  ADMIN("관리자");

  private final String description;

  UserRole(String description) {
    this.description = description;
  }

  public String description() {
    return description;
  }
}
