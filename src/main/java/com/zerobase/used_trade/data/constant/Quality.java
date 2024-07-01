package com.zerobase.used_trade.data.constant;

import com.zerobase.used_trade.data.constant.aware.DescriptionAware;

public enum Quality implements DescriptionAware {
  NEW("신품, 미개봉"),
  MINT("새 것과 다름없음"),
  GREAT("사용감은 있으나 상태 좋음"),
  NORMAL("사용감 있음"),
  ASIS("상태가 좋지 않음");

  private final String description;

  Quality(String description) {
    this.description = description;
  }

  @Override
  public String description() {
    return description;
  }
}
