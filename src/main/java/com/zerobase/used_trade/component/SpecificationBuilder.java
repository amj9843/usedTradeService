package com.zerobase.used_trade.component;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpecificationBuilder<T> {
  public Specification<T> init() {
    return (root, query, criteriaBuilder)-> null;
  }

  public Specification<T> equalString(String key, String value) {
    return (root, query, criteriaBuilder)-> criteriaBuilder.equal(
        criteriaBuilder.upper(root.get(key)), value.toUpperCase());
  }

  public Specification<T> equalNotString(String key, Object value) {
    return (root, query, criteriaBuilder)-> criteriaBuilder.equal(root.get(key), value);
  }

  public Specification<T> like(String key, String value) {
    return (root, query, criteriaBuilder)-> criteriaBuilder.like(
        criteriaBuilder.upper(root.get(key)), "%" + value.toUpperCase() + "%");
  }

  public Specification<T> isBefore(String key, LocalDateTime time) {
    return (root, query, criteriaBuilder)-> criteriaBuilder.lessThanOrEqualTo(root.get(key), time);
  }

  public Specification<T> isAfter(String key, LocalDateTime time) {
    return (root, query, criteriaBuilder)-> criteriaBuilder.greaterThan(root.get(key), time);
  }
}
