package com.zerobase.used_trade.repository.custom;

import com.zerobase.used_trade.data.constant.ReportStatusFilterType;
import com.zerobase.used_trade.data.constant.ReportTypeFilterType;
import com.zerobase.used_trade.data.dto.ReportDto.SimpleInfoResponse;
import java.util.List;

public interface CustomReportRepository {
  List<SimpleInfoResponse> findAllOfSimple(
      Long userId, ReportTypeFilterType typeFilter, ReportStatusFilterType statusFilter);
}
