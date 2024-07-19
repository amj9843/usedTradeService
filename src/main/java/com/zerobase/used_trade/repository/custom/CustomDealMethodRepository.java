package com.zerobase.used_trade.repository.custom;

import com.zerobase.used_trade.data.domain.DealMethod;
import java.util.List;

public interface CustomDealMethodRepository {
  List<DealMethod> findAllByProductIdPossible(Long productId);
}
