package com.zerobase.used_trade.service;

import com.zerobase.used_trade.data.dto.DealMethodDto.EnrollRequest;
import com.zerobase.used_trade.data.dto.DealMethodDto.ProductDetailMethod;

public interface DealMethodService {
  ProductDetailMethod enrollDealMethod(Long userId, EnrollRequest request);

  ProductDetailMethod getDealMethodsByProduct(Long productId);

  void deleteDealMethod(Long userId, Long dealMethodId);
}
