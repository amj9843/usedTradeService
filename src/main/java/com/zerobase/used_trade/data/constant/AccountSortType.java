package com.zerobase.used_trade.data.constant;

import static java.lang.String.format;

import com.zerobase.used_trade.data.comparator.AccountComparator.RepresentativeTopCreatedAtAsc;
import com.zerobase.used_trade.data.constant.aware.DescriptionAware;
import com.zerobase.used_trade.data.constant.aware.SortTypeAware;
import com.zerobase.used_trade.data.domain.Account;
import java.util.Comparator;

public enum AccountSortType implements DescriptionAware, SortTypeAware<Account> {
  REPRESENTATIVETOPCREATEDATASC("대표 계좌 맨 위, 그 외 등록순", new RepresentativeTopCreatedAtAsc());

  private final String description;
  private final Comparator<Account> comparator;

  AccountSortType(String description, Comparator<Account> comparator) {
    this.description = description;
    this.comparator = comparator;
  }

  @Override
  public String description() {
    return description;
  }

  @Override
  public Comparator<Account> comparator() {
    return this.comparator;
  }

  @Override
  public String toString() {
    return format("%s(%s)", this.name(), this.description);
  }
}
