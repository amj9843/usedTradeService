package com.zerobase.used_trade.data.constant;

public enum DealMethodType {
  PARCEL("택배"),
  CONVENIENCE("반값 택배"),
  MEETING("직거래");

  private final String description;

  DealMethodType(String description) {
    this.description = description;
  }

  public String description() {
    return description;
  }
}
