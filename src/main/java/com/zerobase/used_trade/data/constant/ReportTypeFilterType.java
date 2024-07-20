package com.zerobase.used_trade.data.constant;

import static java.lang.String.format;

import com.zerobase.used_trade.data.constant.aware.DescriptionAware;

public enum ReportTypeFilterType implements DescriptionAware {
  ALL("전체", null),
  REPORT("건의사항 필터", ReportType.REPORT),
  SUGGEST("신고 내역 필터", ReportType.SUGGEST);

  final String description;
  final ReportType type;

  ReportTypeFilterType(String description, ReportType type) {
    this.description = description;
    this.type = type;
  }

  public ReportType is() {
    return type;
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
