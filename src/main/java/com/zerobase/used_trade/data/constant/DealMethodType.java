package com.zerobase.used_trade.data.constant;

import static java.lang.String.format;

import com.zerobase.used_trade.data.constant.aware.DescriptionAware;

public enum DealMethodType implements DescriptionAware {
  PARCEL("택배"),
  CONVENIENCE("반값 택배"),
  MEETING("직거래");

  private final String description;

  DealMethodType(String description) {
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
