package com.zerobase.used_trade.repository.custom;

import com.zerobase.used_trade.data.domain.Product;
import java.util.Optional;

public interface CustomProductRepository {
  boolean canUpdatedProductByAdmin(Long userId, Long productId);
  boolean canUpdatedProductByUser(Long userId, Long productId);
  boolean canApplyDeal(Long userId, Long productId);
  boolean isSeller(Long userId, Long productId);
  Optional<Product> findByDealMethodId(Long detailId);
}
