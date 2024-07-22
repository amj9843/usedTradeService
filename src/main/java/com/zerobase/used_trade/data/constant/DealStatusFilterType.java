package com.zerobase.used_trade.data.constant;

import static java.lang.String.format;

import com.zerobase.used_trade.data.constant.aware.DescriptionAware;
import com.zerobase.used_trade.data.constant.aware.EnumFilterTypeAware;

public enum DealStatusFilterType implements DescriptionAware, EnumFilterTypeAware<DealStatus> {
  ALL("전체", null),
  APPLIED("거래 신청 상태 필터", DealStatus.APPLIED),
  APPROVED("거래 승인 상태 필터", DealStatus.APPROVED),
  DENIED("거래 거절 상태", DealStatus.DENIED),
  CANCELED("거래 취소 상태", DealStatus.CANCELED),
  DEPOSITED("입금 완료 상태", DealStatus.DEPOSITED),
  CONFIRMED("입금 확인 상태", DealStatus.CONFIRMED),
  SHIPPING("배송 진행중인 상태", DealStatus.SHIPPING),
  COMPLETED("거래 완료 상태", DealStatus.COMPLETED),
  REFUNDED("환불 완료 상태", DealStatus.REFUNDED);

  final String description;
  final DealStatus status;

  DealStatusFilterType(String description, DealStatus status) {
    this.description = description;
    this.status = status;
  }

  @Override
  public DealStatus is() {
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
