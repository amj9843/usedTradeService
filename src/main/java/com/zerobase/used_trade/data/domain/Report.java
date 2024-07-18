package com.zerobase.used_trade.data.domain;

import com.zerobase.used_trade.data.constant.ReportStatus;
import com.zerobase.used_trade.data.constant.ReportType;
import com.zerobase.used_trade.data.dto.ReportDto.AnswerRequest;
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
public class Report extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "report_id")
  private Long id;

  @Column(name = "reporter_id")
  private Long reporterId;

  @Column(name = "reported_id")
  private Long reportedId;

  @Column(name = "type")
  @Enumerated(EnumType.STRING)
  private ReportType type;

  @Column(name = "title")
  private String title;

  @Column(name = "content")
  private String content;

  @Column(name = "answer")
  private String answer;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private ReportStatus status;

  public void updateAnswer(
      AnswerRequest request) {
    if (request.getAnswer() != null && !request.getAnswer().isBlank()) {
      this.answer = request.getAnswer();
    }
    this.status = ReportStatus.COMPLETED;
  }

  @Builder
  public Report(Long reporterId, Long reportedId, ReportType type, String title, String content, ReportStatus status) {
    this.reporterId = reporterId;
    this.reportedId = reportedId;
    this.type = type;
    this.title = title;
    this.content = content;
    this.status = status;
  }
}
