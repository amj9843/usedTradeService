package com.zerobase.used_trade.data.constant.aware;

import org.springframework.data.jpa.domain.Specification;

public interface FilterTypeAware<T> {
  Specification<T> processing(Specification<T> spec);
}
