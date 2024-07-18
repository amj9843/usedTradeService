package com.zerobase.used_trade.repository.custom.impl;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static java.util.stream.Collectors.toSet;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.used_trade.data.constant.ImageUsing;
import com.zerobase.used_trade.data.constant.ReportStatusFilterType;
import com.zerobase.used_trade.data.constant.ReportTypeFilterType;
import com.zerobase.used_trade.data.domain.QImage;
import com.zerobase.used_trade.data.domain.QReport;
import com.zerobase.used_trade.data.dto.QImageDto_Principle;
import com.zerobase.used_trade.data.dto.QReportDto_SimpleInfoResponse;
import com.zerobase.used_trade.data.dto.ReportDto.SimpleInfoResponse;
import com.zerobase.used_trade.repository.custom.CustomReportRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomReportRepositoryImpl implements CustomReportRepository {
  private final JPAQueryFactory jpaQueryFactory;
  private final QReport report = QReport.report;
  private final QImage image = QImage.image;

  @Override
  public List<SimpleInfoResponse> findAllOfSimple(Long userId, ReportTypeFilterType typeFilter, ReportStatusFilterType statusFilter) {
    List<SimpleInfoResponse> allList = jpaQueryFactory
        .select(new QReportDto_SimpleInfoResponse(
            report.id, report.reporterId, report.reportedId,
            report.type, report.title, report.status, report.createdAt, report.updatedAt
        ))
        .from(report)
        .where(
            reporterId(userId),
            typeFilter(typeFilter),
            statusFilter(statusFilter)
        ).fetch();

    Map<Long, SimpleInfoResponse> haveImageList = jpaQueryFactory
        .from(report)
        .leftJoin(image).on(image.boardId.eq(report.id))
        .where(
            image.used.eq(ImageUsing.REPORT),
            reporterId(userId),
            typeFilter(typeFilter),
            statusFilter(statusFilter)
        )
        .transform(groupBy(report.id).as(new QReportDto_SimpleInfoResponse(
            report.id, report.reporterId, report.reportedId,
            list(new QImageDto_Principle(
                    image.id, image.boardId, image.used, image.src, image.createdAt, image.updatedAt)
            ), report.type, report.title, report.status, report.createdAt, report.updatedAt
            )
        ));

    Set<SimpleInfoResponse> result = haveImageList.keySet().stream()
        .map(haveImageList::get).collect(toSet());
    result.addAll(new HashSet<>(allList));

    return new ArrayList<>(result);
  }

  private BooleanExpression reporterId(Long userId) {
    if (userId == null) {
      return null;
    }

    return report.reporterId.eq(userId);
  }

  private BooleanExpression typeFilter(ReportTypeFilterType type) {
    if (type.is() == null) {
      return null;
    }

    return report.type.eq(type.is());
  }

  private BooleanExpression statusFilter(ReportStatusFilterType status) {
    if (status.is() == null) {
      return null;
    }

    return report.status.eq(status.is());
  }
}
