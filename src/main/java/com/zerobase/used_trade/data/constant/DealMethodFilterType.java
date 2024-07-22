package com.zerobase.used_trade.data.constant;

import static java.lang.String.format;

import com.zerobase.used_trade.data.constant.aware.DescriptionAware;
import com.zerobase.used_trade.data.constant.aware.EnumFilterTypeAware;

public enum DealMethodFilterType implements DescriptionAware, EnumFilterTypeAware<DealMethodType> {
  ALL("전체", null),
  MEETING("직거래 방식 필터", DealMethodType.MEETING),
  PARCEL("택배 방식 필터", DealMethodType.PARCEL),
  CONVENIENCE("반값 택배 방식 필터", DealMethodType.CONVENIENCE);

  final String description;
  final DealMethodType status;

  DealMethodFilterType(String description, DealMethodType status) {
    this.description = description;
    this.status = status;
  }

  @Override
  public DealMethodType is() {
    return this.status;
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
