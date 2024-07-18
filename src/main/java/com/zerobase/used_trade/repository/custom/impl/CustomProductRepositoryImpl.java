package com.zerobase.used_trade.repository.custom.impl;

import static com.zerobase.used_trade.data.constant.ProductStatus.CONSIGNMENT_APPLY;
import static com.zerobase.used_trade.data.constant.ProductStatus.CONSIGNMENT_APPROVED;
import static com.zerobase.used_trade.data.constant.ProductStatus.SELLING;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.used_trade.data.domain.QConsignment;
import com.zerobase.used_trade.data.domain.QProduct;
import com.zerobase.used_trade.repository.custom.CustomProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomProductRepositoryImpl implements CustomProductRepository {
  private final JPAQueryFactory jpaQueryFactory;
  private final QProduct product = QProduct.product;
  private final QConsignment consignment = QConsignment.consignment;

  @Override
  public boolean canUpdatedProductByAdmin(Long userId, Long productId) {
    Integer fetchOne = jpaQueryFactory
        .selectOne()
        .from(product)
        .leftJoin(consignment).on(product.id.eq(consignment.productId))
        .where(
            product.id.eq(productId),
            consignment.adminId.eq(userId),
            product.status.eq(CONSIGNMENT_APPROVED)
                    .or(product.status.eq(SELLING))
        )
        .fetchFirst();

    return fetchOne != null;
  }

  @Override
  public boolean canUpdatedProductByUser(Long userId, Long productId) {
    Integer fetchOne = jpaQueryFactory
        .selectOne()
        .from(product)
        .leftJoin(consignment).on(product.id.eq(consignment.productId))
        .where(
            product.id.eq(productId),
            product.sellerId.eq(userId),
            (
                product.consignment.eq(false).and(product.status.eq(SELLING))
            ).or(
                (product.consignment.eq(true).and(product.status.eq(CONSIGNMENT_APPLY)))
            )
        )
        .fetchFirst();

    return fetchOne != null;
  }
}
