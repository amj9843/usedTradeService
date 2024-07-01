package com.zerobase.used_trade.data.constant;

import com.zerobase.used_trade.data.constant.aware.DescriptionAware;

public enum ReportType implements DescriptionAware {
  REPORT("신고"),
  SUGGEST("건의");

  private final String description;

  ReportType(String description) {
    this.description = description;
  }

  @Override
  public String description() {
    return description;
  }
}
