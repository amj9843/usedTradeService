package com.zerobase.used_trade.repository;

import com.zerobase.used_trade.data.domain.ProductMatchCategory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductMatchCategoryRepository extends JpaRepository<ProductMatchCategory, Long> {
  boolean existsByCategoryId(Long categoryId);

  Optional<ProductMatchCategory> findByProductIdAndCategoryId(Long productId, Long categoryId);
}
