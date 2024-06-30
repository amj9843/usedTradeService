package com.zerobase.used_trade.data.constant;

public enum ReportType {
  REPORT("신고"),
  SUGGEST("건의");

  private final String description;

  ReportType(String description) {
    this.description = description;
  }

  public String description() {
    return description;
  }
}
