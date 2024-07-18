package com.zerobase.used_trade.repository;

import com.zerobase.used_trade.data.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{
  boolean existsByIdAndBuyerId(Long reviewId, Long userId);
}
