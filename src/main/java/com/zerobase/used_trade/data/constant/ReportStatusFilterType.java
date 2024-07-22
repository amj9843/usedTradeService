package com.zerobase.used_trade.data.constant;

import static java.lang.String.format;

import com.zerobase.used_trade.data.constant.aware.DescriptionAware;
import com.zerobase.used_trade.data.constant.aware.EnumFilterTypeAware;

public enum ReportStatusFilterType implements DescriptionAware, EnumFilterTypeAware<ReportStatus> {
  ALL("전체", null),
  NOTCOMPLETED("처리 전 결과 필터", ReportStatus.REGIST),
  COMPLETED("처리 후 필터", ReportStatus.COMPLETED);

  final String description;
  final ReportStatus status;

  ReportStatusFilterType(String description, ReportStatus status) {
    this.description = description;
    this.status = status;
  }

  @Override
  public ReportStatus is() {
    return this.status;
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
