package com.zerobase.used_trade.data.comparator;

import com.zerobase.used_trade.data.dto.ReportDto.SimpleInfoResponse;
import java.util.Comparator;

public class ReportComparator {

  public static class CreatedAtDesc implements Comparator<SimpleInfoResponse> {
    @Override
    public int compare(SimpleInfoResponse o1, SimpleInfoResponse o2) {
      return o2.getReportId().compareTo(o1.getReportId());
    }
  }

  public static class CreatedAsc implements Comparator<SimpleInfoResponse> {
    @Override
    public int compare(SimpleInfoResponse o1, SimpleInfoResponse o2) {
      return o1.getReportId().compareTo(o2.getReportId());
    }
  }
}
