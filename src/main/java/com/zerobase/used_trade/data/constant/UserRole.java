package com.zerobase.used_trade.data.constant;

import static java.lang.String.format;

import com.zerobase.used_trade.data.constant.aware.DescriptionAware;

public enum UserRole implements DescriptionAware {
  USER("일반 사용자"),
  DENIED("거래 불가자"),
  ADMIN("관리자");

  private final String description;

  UserRole(String description) {
    this.description = description;
  }

  @Override
  public String description() {
    return description;
  }

  @Override
  public String toString() {
    return format("%s(%s)", this.name(), this.description);
  }
}
