package com.zerobase.used_trade.repository.custom.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.used_trade.data.constant.ProductStatus;
import com.zerobase.used_trade.data.domain.DealMethod;
import com.zerobase.used_trade.data.domain.QDeal;
import com.zerobase.used_trade.data.domain.QDealMethod;
import com.zerobase.used_trade.data.domain.QProduct;
import com.zerobase.used_trade.repository.custom.CustomDealMethodRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomDealMethodRepositoryImpl implements CustomDealMethodRepository {
  private final JPAQueryFactory jpaQueryFactory;

  private final QDealMethod dealMethod = QDealMethod.dealMethod;
  private final QProduct product = QProduct.product;
  private final QDeal deal = QDeal.deal;

  @Override
  public List<DealMethod> findAllByProductIdPossible(Long productId) {
    return jpaQueryFactory
        .select(dealMethod)
        .from(dealMethod)
        .leftJoin(product).on(product.id.eq(dealMethod.productId))
        .where(
            dealMethod.productId.eq(productId),
            product.status.eq(ProductStatus.PROCESSING)
                    .or(product.status.eq(ProductStatus.SELLING)),
            dealMethod.dateTime.isNull().or(
                dealMethod.dateTime.after(LocalDateTime.now())
            )
        ).orderBy(dealMethod.dateTime.asc().nullsFirst())
        .fetch();
  }

  @Override
  public Optional<DealMethod> findByDealId(Long dealId) {
    return Optional.ofNullable(jpaQueryFactory
        .select(dealMethod)
        .from(dealMethod)
        .leftJoin(deal).on(deal.detailId.eq(dealMethod.id))
        .where(
            deal.id.eq(dealId)
        ).fetchFirst());
  }
}
