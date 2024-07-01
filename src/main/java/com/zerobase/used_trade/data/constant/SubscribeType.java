package com.zerobase.used_trade.data.constant;

import com.zerobase.used_trade.data.constant.aware.DescriptionAware;
import java.time.LocalDateTime;

public enum SubscribeType implements DescriptionAware {
  ONEYEAR("1년(12개월)", 12),
  HALFYEAR("반년(6개월)", 6),
  ONEMONTH("한 달(1개월)", 1);

  private final String description;
  private final long expirationPeriod;

  SubscribeType(String description, long expirationPeriod) {
    this.description = description;
    this.expirationPeriod = expirationPeriod;
  }

  @Override
  public String description() {
    return description;
  }

  public LocalDateTime extension(LocalDateTime datetime) {
    return datetime.plusMonths(expirationPeriod);
  }
}
