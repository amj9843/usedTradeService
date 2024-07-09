package com.zerobase.used_trade.data.constant;

import static java.lang.String.format;

import com.zerobase.used_trade.data.comparator.DomainComparator.CreatedAsc;
import com.zerobase.used_trade.data.comparator.DomainComparator.CreatedAtDesc;
import com.zerobase.used_trade.data.comparator.DomainComparator.EndAtAsc;
import com.zerobase.used_trade.data.comparator.DomainComparator.NameAsc;
import com.zerobase.used_trade.data.constant.aware.DescriptionAware;
import com.zerobase.used_trade.data.constant.aware.SortTypeAware;
import com.zerobase.used_trade.data.domain.Domain;
import java.util.Comparator;

public enum DomainSortType implements DescriptionAware, SortTypeAware<Domain> {
  NAMEASC("회사명 순", new NameAsc()),
  CREATEDATDESC("최근 등록순", new CreatedAtDesc()),
  CREATEDATASC("등록순", new CreatedAsc()),
  ENDATASC("만료일순", new EndAtAsc());

  private final String description;
  private final Comparator<Domain> comparator;

  DomainSortType(String description, Comparator<Domain> comparator) {
    this.description = description;
    this.comparator = comparator;
  }

  @Override
  public String description() {
    return description;
  }

  @Override
  public Comparator<Domain> comparator() {
    return this.comparator;
  }

  @Override
  public String toString() {
    return format("%s(%s)", this.name(), this.description);
  }
}
