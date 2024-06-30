package com.zerobase.used_trade.data.constant;

public enum ReportStatus {
  REGIST("등록된 상태"),
  COMPLETED("처리/답변 완료된 상태");

  private final String description;

  ReportStatus(String description) {
    this.description = description;
  }

  public String description() {
    return description;
  }
}
