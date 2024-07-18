package com.zerobase.used_trade.service;

import com.zerobase.used_trade.data.constant.ReportSortType;
import com.zerobase.used_trade.data.constant.ReportStatusFilterType;
import com.zerobase.used_trade.data.constant.ReportTypeFilterType;
import com.zerobase.used_trade.data.dto.ReportDto.AnswerRequest;
import com.zerobase.used_trade.data.dto.ReportDto.EnrollRequest;
import com.zerobase.used_trade.data.dto.ReportDto.Principle;
import com.zerobase.used_trade.data.dto.ReportDto.SimpleInfoResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface ReportService {
  Principle enrollReport(Long userId, List<MultipartFile> images, EnrollRequest request);

  void enrollAnswer(Long reportId, Long userId, AnswerRequest request);

  Page<SimpleInfoResponse> getReportList(Long userId, int page, int size,
      ReportTypeFilterType typeFilter, ReportStatusFilterType statusFilter, ReportSortType sort);

  Principle getReportDetail(Long userId, Long reportId);
}
