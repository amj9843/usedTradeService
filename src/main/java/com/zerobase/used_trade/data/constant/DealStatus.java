package com.zerobase.used_trade.data.constant;

import com.zerobase.used_trade.data.constant.aware.DescriptionAware;

public enum DealStatus implements DescriptionAware {
  APPLIED("거래 신청 상태"),
  APPROVED("거래 승인 상태"),
  DENIED("거래 거절 상태"),
  CANCELED("거래 취소 상태"),
  DEPOSITED("입금 완료 상태"),
  CONFIRMED("입금 확인 상태"),
  SHIPPING("배송 진행중인 상태"),
  COMPLETED("거래 완료 상태"),
  REFUNDED("환불 완료 상태");

  private final String description;

  DealStatus(String description) {
    this.description = description;
  }

  @Override
  public String description() {
    return description;
  }
}
