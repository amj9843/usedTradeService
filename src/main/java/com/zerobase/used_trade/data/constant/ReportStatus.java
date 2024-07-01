package com.zerobase.used_trade.data.constant;

import com.zerobase.used_trade.data.constant.aware.DescriptionAware;

public enum ReportStatus implements DescriptionAware {
  REGIST("등록된 상태"),
  COMPLETED("처리/답변 완료된 상태");

  private final String description;

  ReportStatus(String description) {
    this.description = description;
  }

  @Override
  public String description() {
    return description;
  }
}
