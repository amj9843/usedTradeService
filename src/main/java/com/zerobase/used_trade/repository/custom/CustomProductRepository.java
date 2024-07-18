package com.zerobase.used_trade.repository.custom;

public interface CustomProductRepository {
  boolean canUpdatedProductByAdmin(Long userId, Long productId);
  boolean canUpdatedProductByUser(Long userId, Long productId);
}
