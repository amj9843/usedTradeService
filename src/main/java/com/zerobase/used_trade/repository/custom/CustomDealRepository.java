package com.zerobase.used_trade.repository.custom;

public interface CustomDealRepository {
  boolean isApplyingOrProcessingByDealMethodId(Long dealMethodId);
}
