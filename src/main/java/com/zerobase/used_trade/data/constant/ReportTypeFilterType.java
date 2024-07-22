package com.zerobase.used_trade.data.constant;

import static java.lang.String.format;

import com.zerobase.used_trade.data.constant.aware.DescriptionAware;
import com.zerobase.used_trade.data.constant.aware.EnumFilterTypeAware;

public enum ReportTypeFilterType implements DescriptionAware, EnumFilterTypeAware<ReportType> {
  ALL("전체", null),
  REPORT("건의사항 필터", ReportType.REPORT),
  SUGGEST("신고 내역 필터", ReportType.SUGGEST);

  final String description;
  final ReportType type;

  ReportTypeFilterType(String description, ReportType type) {
    this.description = description;
    this.type = type;
  }

  @Override
  public ReportType is() {
    return this.type;
  }

  @Override
  public String description() {
    return this.description;
  }

  @Override
  public String toString() {
    return format("%s(%s)", this.name(), this.description);
  }
}
