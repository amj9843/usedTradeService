package com.zerobase.used_trade.data.constant;

import static java.lang.String.format;

import com.zerobase.used_trade.data.comparator.AddressComparator.RepresentativeTopCreatedAtAsc;
import com.zerobase.used_trade.data.constant.aware.DescriptionAware;
import com.zerobase.used_trade.data.constant.aware.SortTypeAware;
import com.zerobase.used_trade.data.domain.Address;
import java.util.Comparator;

public enum AddressSortType implements DescriptionAware, SortTypeAware<Address> {
  REPRESENTATIVETOPCREATEDATASC("대표 주소 맨 위, 그 외 등록순", new RepresentativeTopCreatedAtAsc());

  private final String description;
  private final Comparator<Address> comparator;

  AddressSortType(String description, Comparator<Address> comparator) {
    this.description = description;
    this.comparator = comparator;
  }

  @Override
  public String description() {
    return description;
  }

  @Override
  public Comparator<Address> comparator() {
    return this.comparator;
  }

  @Override
  public String toString() {
    return format("%s(%s)", this.name(), this.description);
  }
}
