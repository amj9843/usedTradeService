package com.zerobase.used_trade.data.constant;

import static java.lang.String.format;

import com.zerobase.used_trade.data.comparator.ReportComparator.CreatedAtAsc;
import com.zerobase.used_trade.data.comparator.ReportComparator.CreatedAtDesc;
import com.zerobase.used_trade.data.constant.aware.DescriptionAware;
import com.zerobase.used_trade.data.constant.aware.SortTypeAware;
import com.zerobase.used_trade.data.dto.ReportDto.SimpleInfoResponse;
import java.util.Comparator;

public enum ReportSortType implements DescriptionAware, SortTypeAware<SimpleInfoResponse> {
  CREATEDATDESC("최근 등록순", new CreatedAtDesc()),
  CREATEDATASC("등록순", new CreatedAtAsc());

  private final String description;
  private final Comparator<SimpleInfoResponse> comparator;

  ReportSortType(String description, Comparator<SimpleInfoResponse> comparator) {
    this.description = description;
    this.comparator = comparator;
  }

  @Override
  public String description() {
    return description;
  }

  @Override
  public Comparator<SimpleInfoResponse> comparator() {
    return this.comparator;
  }

  @Override
  public String toString() {
    return format("%s(%s)", this.name(), this.description);
  }
}
