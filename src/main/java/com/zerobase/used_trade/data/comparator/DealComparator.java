package com.zerobase.used_trade.data.comparator;

import com.zerobase.used_trade.data.dto.DealDto.SimpleInfoResponse;
import java.util.Comparator;

public class DealComparator {
  public static class CreatedAtDesc implements Comparator<SimpleInfoResponse> {
    @Override
    public int compare(SimpleInfoResponse o1, SimpleInfoResponse o2) {
      return o2.getId().compareTo(o1.getId());
    }
  }

  public static class CreatedAtAsc implements Comparator<SimpleInfoResponse> {
    @Override
    public int compare(SimpleInfoResponse o1, SimpleInfoResponse o2) {
      return o1.getId().compareTo(o2.getId());
    }
  }
}
