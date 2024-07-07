package com.zerobase.used_trade.data.constant;

import static java.lang.String.format;

import com.zerobase.used_trade.data.constant.aware.DescriptionAware;

public enum ImageUsing implements DescriptionAware {
  PRODUCT("상품 이미지"),
  REPORT("신고/건의 이미지"),
  REVIEW("후기 이미지");

  private final String description;

  ImageUsing(String description) {
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
