package com.zerobase.used_trade.data.constant;

import com.zerobase.used_trade.data.constant.aware.DescriptionAware;

public enum ProductStatus implements DescriptionAware {
  CONSIGNMENT_APPLY("위탁 신청 상태"),
  CONSIGNMENT_APPROVED("위탁 승인 상태"),
  SELLING("판매중인 상태"),
  PROCESSING("거래가 진행중인 상태"),
  SELLED("판매 완료 상태"),
  CANCELED_APPLY("위탁 취소 신청 상태"),
  CANCELED("판매 취소 상태");

  private final String description;

  ProductStatus(String description) {
    this.description = description;
  }

  @Override
  public String description() {
    return description;
  }
}
