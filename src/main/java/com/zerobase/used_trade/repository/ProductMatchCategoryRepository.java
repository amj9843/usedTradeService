package com.zerobase.used_trade.repository;

import com.zerobase.used_trade.data.domain.ProductMatchCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductMatchCategoryRepository extends JpaRepository<ProductMatchCategory, Long> {
  boolean existsByCategoryId(Long categoryId);
}
