package com.zerobase.used_trade.data.constant;

import com.zerobase.used_trade.data.constant.aware.DescriptionAware;
import com.zerobase.used_trade.data.constant.aware.FilterTypeAware;
import com.zerobase.used_trade.data.domain.Domain;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

public enum DomainFilterType implements DescriptionAware, FilterTypeAware<Domain> {
  ALL("전체", list -> list),
  VALID("유효한 결과 필터", list->
      list.stream().filter(obj -> LocalDateTime.now().isBefore(obj.getEndAt())).toList()),
  EXPIRED("만료된 결과 필터", list->
      list.stream().filter(obj -> LocalDateTime.now().isAfter(obj.getEndAt())).toList());

  final String description;
  final Function<List<Domain>, List<Domain>> function;

  DomainFilterType(String description, Function<List<Domain>, List<Domain>> function) {
    this.description = description;
    this.function = function;
  }

  @Override
  public String description() {
    return description;
  }

  @Override
  public List<Domain> processing(List<Domain> list) {
    return this.function.apply(list);
  }
}
