package com.zerobase.used_trade.repository.custom;

import com.zerobase.used_trade.data.domain.DealMethod;
import java.util.List;
import java.util.Optional;

public interface CustomDealMethodRepository {
  List<DealMethod> findAllByProductIdPossible(Long productId);
  Optional<DealMethod> findByDealId(Long dealId);
}
