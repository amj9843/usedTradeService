package com.zerobase.used_trade.data.constant;

import static java.lang.String.format;

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

  public LocalDateTime extension(LocalDateTime datetime) {
    return datetime.plusMonths(expirationPeriod);
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
