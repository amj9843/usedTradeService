package com.zerobase.used_trade.repository;

import com.zerobase.used_trade.data.domain.Report;
import com.zerobase.used_trade.repository.custom.CustomReportRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long>, CustomReportRepository {

}
