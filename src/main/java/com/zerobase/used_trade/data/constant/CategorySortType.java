package com.zerobase.used_trade.data.constant;

import static java.lang.String.format;

import com.zerobase.used_trade.data.comparator.CategoryComparator.CreatedAtAsc;
import com.zerobase.used_trade.data.comparator.CategoryComparator.CreatedAtDesc;
import com.zerobase.used_trade.data.comparator.CategoryComparator.NameAsc;
import com.zerobase.used_trade.data.constant.aware.DescriptionAware;
import com.zerobase.used_trade.data.constant.aware.SortTypeAware;
import com.zerobase.used_trade.data.domain.Category;
import java.util.Comparator;

public enum CategorySortType implements DescriptionAware, SortTypeAware<Category> {
  NAMEASC("이름순", new NameAsc()),
  CREATEDATDESC("최근 등록순", new CreatedAtDesc()),
  CREATEDATASC("등록순", new CreatedAtAsc());

  private final String description;
  private final Comparator<Category> comparator;

  CategorySortType(String description, Comparator<Category> comparator) {
    this.description = description;
    this.comparator = comparator;
  }

  @Override
  public String description() {
    return description;
  }

  @Override
  public Comparator<Category> comparator() {
    return this.comparator;
  }

  @Override
  public String toString() {
    return format("%s(%s)", this.name(), this.description);
  }
}
