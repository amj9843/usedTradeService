package com.zerobase.used_trade.data.constant.aware;

import java.util.Comparator;

public interface SortTypeAware<T> {
  Comparator<T> comparator();
}
