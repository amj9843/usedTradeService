package com.zerobase.used_trade.repository.custom;

import com.zerobase.used_trade.data.constant.DealMethodFilterType;
import com.zerobase.used_trade.data.constant.DealStatusFilterType;
import com.zerobase.used_trade.data.domain.Deal;
import com.zerobase.used_trade.data.dto.DealDto.SimpleInfoResponse;
import java.time.LocalDateTime;
import java.util.List;

public interface CustomDealRepository {
  boolean isApplyingOrProcessingByDealMethodId(Long dealMethodId);

  List<SimpleInfoResponse> findAllOfSimple(
      Long productId, Long buyerId, LocalDateTime applyTimeAfter, LocalDateTime applyTimeBefore,
      DealMethodFilterType methodFilter, DealStatusFilterType statusFilter);
  boolean isSeller(Long userId, Long dealId);
  List<Deal> findAllByProductIdExceptDealId(Long productId, Long dealId);
}
