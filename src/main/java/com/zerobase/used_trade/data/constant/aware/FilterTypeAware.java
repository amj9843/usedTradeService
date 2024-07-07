package com.zerobase.used_trade.data.constant.aware;

import java.util.List;

public interface FilterTypeAware<T> {
  List<T> processing(List<T> list);
}
