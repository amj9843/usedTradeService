package com.zerobase.used_trade.data.constant;

import static java.lang.String.format;

import com.zerobase.used_trade.data.comparator.DealComparator.CreatedAtAsc;
import com.zerobase.used_trade.data.comparator.DealComparator.CreatedAtDesc;
import com.zerobase.used_trade.data.constant.aware.DescriptionAware;
import com.zerobase.used_trade.data.constant.aware.SortTypeAware;
import com.zerobase.used_trade.data.dto.DealDto.SimpleInfoResponse;
import java.util.Comparator;

public enum DealSortType implements DescriptionAware, SortTypeAware<SimpleInfoResponse> {
  CREATEDATASC("거래 신청 순", new CreatedAtAsc()),
  CREATEDATDESC("최근 거래 신청 순", new CreatedAtDesc());

  private final String description;
  private final Comparator<SimpleInfoResponse> comparator;

  DealSortType(String description, Comparator<SimpleInfoResponse> comparator) {
    this.description = description;
    this.comparator = comparator;
  }

  @Override
  public String description()  {
      return this.description;
  }

  @Override
  public Comparator<SimpleInfoResponse> comparator()  {
      return this.comparator;
  }

  @Override
  public String toString() {
    return format("%s(%s)", this.name(), this.description);
  }
}
