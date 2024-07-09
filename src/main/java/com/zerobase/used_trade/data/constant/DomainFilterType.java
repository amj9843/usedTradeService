package com.zerobase.used_trade.data.constant;

import com.zerobase.used_trade.component.SpecificationBuilder;
import com.zerobase.used_trade.data.constant.aware.DescriptionAware;
import com.zerobase.used_trade.data.constant.aware.FilterTypeAware;
import com.zerobase.used_trade.data.domain.Domain;
import java.time.LocalDateTime;
import java.util.function.Function;
import org.springframework.data.jpa.domain.Specification;

public enum DomainFilterType implements DescriptionAware, FilterTypeAware<Domain> {
  ALL("전체", spec-> spec),
  VALID("유효한 결과 필터", spec-> spec.and(
      new SpecificationBuilder<Domain>().isAfter("endAt", LocalDateTime.now()))),
  EXPIRED("만료된 결과 필터", spec-> spec.and(
      new SpecificationBuilder<Domain>().isBefore("endAt", LocalDateTime.now())));

  final String description;
  final Function<Specification<Domain>, Specification<Domain>> function;

  DomainFilterType(String description, Function<Specification<Domain>, Specification<Domain>> function) {
    this.description = description;
    this.function = function;
  }

  @Override
  public String description() {
    return description;
  }

  @Override
  public Specification<Domain> processing(Specification<Domain> spec) {
    return this.function.apply(spec);
  }
}
