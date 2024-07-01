package com.zerobase.used_trade.data.domain;

import com.zerobase.used_trade.data.constant.ReportStatus;
import com.zerobase.used_trade.data.constant.ReportType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "report")
@Entity
public class Report {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "report_id")
  private Long id;

  @Column(name = "reporter_id")
  private Long reporterId;

  @Column(name = "reported_id")
  private Long reportedId;

  @Column(name = "type")
  private ReportType type;

  @Column(name = "content")
  private String content;

  @Column(name = "answer")
  private String answer;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private ReportStatus status;

  public void updateAnswer(
      String answer) {
    this.answer = answer;
    this.status = ReportStatus.COMPLETED;
  }

  @Builder
  public Report(Long reporterId, Long reporteeId, ReportType type, String content, ReportStatus status) {
    this.reporterId = reporterId;
    this.reportedId = reporteeId;
    this.type = type;
    this.content = content;
    this.status = status;
  }
}
