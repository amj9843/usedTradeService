package com.zerobase.used_trade.data.constant;

import com.zerobase.used_trade.data.constant.aware.DescriptionAware;

public enum PatternType implements DescriptionAware {
  BUSINESS_NUMBER("사업자 등록번호 패턴", "(^\\d{3}-\\d{2}-\\d{5}$)|(^\\d{10}$)"),
  DOMAIN_ADDRESS("도메인 주소 패턴", "^((\\w+-?)+\\.)+(\\w+-?)+$"),
  PHONE_NUMBER("핸드폰 번호 패턴", "^\\d{2,3}-\\d{3,4}-\\d{4}$"),
  ZIP_CODE("우편번호 패턴", "^\\d{5}$"),
  PASSWORD("비밀번호 패턴", "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*?_]).{8,}$"),
  ONLY_NUMBER("숫자만 있는 경우", "^\\d+$");

  private final String description;
  private final String regex;

  PatternType(String description, String regex) {
    this.description = description;
    this.regex = regex;
  }

  @Override
  public String description() {
    return this.description;
  }

  public String regex() {
    return this.regex;
  }
}
