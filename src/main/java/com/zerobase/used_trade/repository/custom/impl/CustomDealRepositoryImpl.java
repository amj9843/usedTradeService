package com.zerobase.used_trade.repository.custom.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.used_trade.data.constant.DealStatus;
import com.zerobase.used_trade.data.domain.QDeal;
import com.zerobase.used_trade.repository.custom.CustomDealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomDealRepositoryImpl implements CustomDealRepository {
  private final JPAQueryFactory jpaQueryFactory;

  private final QDeal deal = QDeal.deal;

  @Override
  public boolean isApplyingOrProcessingByDealMethodId(Long dealMethodId) {
    Integer fetchOne = jpaQueryFactory
        .selectOne()
        .from(deal)
        .where(
            deal.detailId.eq(dealMethodId),
            deal.status.ne(DealStatus.REFUNDED)
                .and(deal.status.ne(DealStatus.CANCELED))
                .and(deal.status.ne(DealStatus.COMPLETED))
                .and(deal.status.ne(DealStatus.DENIED))
        )
        .fetchFirst();

    return fetchOne != null;
  }
}
