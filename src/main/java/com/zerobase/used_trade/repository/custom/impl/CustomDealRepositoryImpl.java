package com.zerobase.used_trade.repository.custom.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.used_trade.data.constant.DealMethodFilterType;
import com.zerobase.used_trade.data.constant.DealStatus;
import com.zerobase.used_trade.data.constant.DealStatusFilterType;
import com.zerobase.used_trade.data.domain.Deal;
import com.zerobase.used_trade.data.domain.QConsignment;
import com.zerobase.used_trade.data.domain.QDeal;
import com.zerobase.used_trade.data.domain.QDealMethod;
import com.zerobase.used_trade.data.domain.QProduct;
import com.zerobase.used_trade.data.dto.DealDto.SimpleInfoResponse;
import com.zerobase.used_trade.data.dto.QDealDto_SimpleInfoResponse;
import com.zerobase.used_trade.repository.custom.CustomDealRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomDealRepositoryImpl implements CustomDealRepository {
  private final JPAQueryFactory jpaQueryFactory;

  private final QDeal deal = QDeal.deal;
  private final QProduct product = QProduct.product;
  private final QDealMethod dealMethod = QDealMethod.dealMethod;
  private final QConsignment consignment = QConsignment.consignment;

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

  @Override
  public List<SimpleInfoResponse> findAllOfSimple(Long productId, Long buyerId, LocalDateTime applyTimeAfter,
      LocalDateTime applyTimeBefore, DealMethodFilterType methodFilter, DealStatusFilterType statusFilter) {
    return jpaQueryFactory
        .select(new QDealDto_SimpleInfoResponse(
            deal.id, deal.createdAt, dealMethod.type, deal.buyerId, deal.status
        )).from(deal)
        .leftJoin(dealMethod).on(dealMethod.id.eq(deal.detailId))
        .leftJoin(product).on(product.id.eq(dealMethod.productId))
        .where(
            product.id.eq(productId),
            buyerId(buyerId),
            applyTimeAfter(applyTimeAfter),
            applyTimeBefore(applyTimeBefore),
            methodFilter(methodFilter),
            statusFilter(statusFilter)
        ).fetch();
  }

  @Override
  public boolean isSeller(Long userId, Long dealId) {
    Integer fetchOne = jpaQueryFactory
        .selectOne()
        .from(deal)
        .leftJoin(dealMethod).on(dealMethod.id.eq(deal.detailId))
        .leftJoin(product).on(product.id.eq(dealMethod.productId))
        .where(
            deal.id.eq(dealId),
            product.consignment.isFalse(),
            product.sellerId.eq(userId)
        )
        .fetchFirst();

    if (fetchOne == null) {
      fetchOne = jpaQueryFactory
          .selectOne()
          .from(deal)
          .leftJoin(dealMethod).on(dealMethod.id.eq(deal.detailId))
          .leftJoin(product).on(product.id.eq(dealMethod.productId))
          .leftJoin(consignment).on(consignment.productId.eq(product.id))
          .where(
              deal.id.eq(dealId),
              product.consignment.isTrue(),
              consignment.adminId.eq(userId)
          )
          .fetchFirst();
    }

    return fetchOne != null;
  }

  @Override
  public List<Deal> findAllByProductIdExceptDealId(Long productId, Long dealId) {
    return jpaQueryFactory
        .select(deal)
        .from(deal)
        .leftJoin(dealMethod).on(dealMethod.id.eq(deal.detailId))
        .leftJoin(product).on(product.id.eq(dealMethod.productId))
        .where(
            product.id.eq(productId),
            deal.id.ne(dealId)
        )
        .fetch();
  }

  private BooleanExpression buyerId(Long userId) {
    if (userId == null) {
      return null;
    }

    return deal.buyerId.eq(userId);
  }

  private BooleanExpression applyTimeAfter(LocalDateTime applyTimeAfter) {
    if (applyTimeAfter == null) {
      return null;
    }

    return deal.createdAt.after(applyTimeAfter);
  }

  private BooleanExpression applyTimeBefore(LocalDateTime applyTimeBefore) {
    if (applyTimeBefore == null) {
      return null;
    }

    return deal.createdAt.before(applyTimeBefore);
  }

  private BooleanExpression methodFilter(DealMethodFilterType type) {
    if (type.is() == null) {
      return null;
    }

    return dealMethod.type.eq(type.is());
  }

  private BooleanExpression statusFilter(DealStatusFilterType type) {
    if (type.is() == null) {
      return null;
    }

    return deal.status.eq(type.is());
  }
}
