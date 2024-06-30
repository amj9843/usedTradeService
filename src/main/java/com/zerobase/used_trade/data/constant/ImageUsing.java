package com.zerobase.used_trade.data.constant;

public enum ImageUsing {
  PRODUCT("상품 이미지"),
  REPORT("신고/건의 이미지"),
  REVIEW("후기 이미지");

  private final String description;

  ImageUsing(String description) {
    this.description = description;
  }

  public String description() {
    return description;
  }
}
