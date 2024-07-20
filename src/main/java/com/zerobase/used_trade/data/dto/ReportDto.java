package com.zerobase.used_trade.data.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.zerobase.used_trade.annotation.EmptyOrNotBlank;
import com.zerobase.used_trade.annotation.EntityId;
import com.zerobase.used_trade.annotation.ValidEnum;
import com.zerobase.used_trade.data.constant.ReportStatus;
import com.zerobase.used_trade.data.constant.ReportType;
import com.zerobase.used_trade.data.domain.Report;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

public class ReportDto {
  @Data
  @Builder
  public static class Principle {
    private Long id;
    private Long reporterId;
    private Long reportedUserId;
    private List<ImageDto.Principle> images;
    private ReportType type;
    private String title;
    private String content;
    private String answer;
    private ReportStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Principle fromEntity(Report report, List<ImageDto.Principle> images) {
      return Principle.builder()
          .id(report.getId())
          .reporterId(report.getReporterId())
          .reportedUserId(report.getReportedUserId())
          .images(images)
          .type(report.getType())
          .title(report.getTitle())
          .content(report.getContent())
          .answer(report.getAnswer())
          .status(report.getStatus())
          .createdAt(report.getCreatedAt())
          .updatedAt(report.getUpdatedAt())
          .build();
    }
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class EnrollRequest {
    @EntityId(message = "{validation.Report.reportedUserId}")
    private Long reportedUserId;

    @NotEmpty(message = "{validation.Report.type.NotEmpty}")
    @ValidEnum(enumClass = ReportType.class)
    private String type;

    @NotBlank(message = "{validation.Report.title.NotBlank}")
    private String title;

    @NotBlank(message = "{validation.Report.content.NotBlank}")
    private String content;

    public Report toEntity(Long userId) {
      return Report.builder()
          .reporterId(userId)
          .reportedUserId(this.reportedUserId)
          .type(ReportType.valueOf(this.type))
          .title(this.title)
          .content(this.content)
          .status(ReportStatus.REGIST)
          .build();
    }
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class AnswerRequest {
    @EmptyOrNotBlank
    private String answer;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(of = {"reportId"})
  public static class SimpleInfoResponse{
    private Long reportId;
    private Long reporterId;
    private Long reportedUserId;
    private List<ImageDto.Principle> images;
    private ReportType type;
    private String title;
    private ReportStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @QueryProjection
    public SimpleInfoResponse(Long reportId, Long reporterId, Long reportedUserId,
        List<ImageDto.Principle> images, ReportType type, String title, ReportStatus status,
        LocalDateTime createdAt, LocalDateTime updatedAt) {
      this.reportId = reportId;
      this.reporterId = reporterId;
      this.reportedUserId = reportedUserId;
      this.images = images;
      this.type = type;
      this.title = title;
      this.status = status;
      this.createdAt = createdAt;
      this.updatedAt = updatedAt;
    }

    @QueryProjection
    public SimpleInfoResponse(Long reportId, Long reporterId, Long reportedUserId,
        ReportType type, String title, ReportStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
      this.reportId = reportId;
      this.reporterId = reporterId;
      this.reportedUserId = reportedUserId;
      this.type = type;
      this.title = title;
      this.status = status;
      this.createdAt = createdAt;
      this.updatedAt = updatedAt;
    }
  }
}
