package com.zerobase.used_trade.repository.custom.impl;

import static com.zerobase.used_trade.data.constant.ProductStatus.CONSIGNMENT_APPLY;
import static com.zerobase.used_trade.data.constant.ProductStatus.CONSIGNMENT_APPROVED;
import static com.zerobase.used_trade.data.constant.ProductStatus.SELLING;
import static com.zerobase.used_trade.util.GroupStatusUtility.CAN_APPLY_DEAL_PRODUCT_STATUS;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.used_trade.data.domain.Product;
import com.zerobase.used_trade.data.domain.QConsignment;
import com.zerobase.used_trade.data.domain.QDealMethod;
import com.zerobase.used_trade.data.domain.QProduct;
import com.zerobase.used_trade.repository.custom.CustomProductRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomProductRepositoryImpl implements CustomProductRepository {
  private final JPAQueryFactory jpaQueryFactory;

  private final QProduct product = QProduct.product;
  private final QDealMethod dealMethod = QDealMethod.dealMethod;
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
        .where(
            product.id.eq(productId),
            product.sellerId.eq(userId),
            (
                product.consignment.eq(false).and(product.status.eq(SELLING))
            ).or(
                product.consignment.eq(true).and(product.status.eq(CONSIGNMENT_APPLY)
                    .or(product.status.eq(CONSIGNMENT_APPROVED)))
            )
        )
        .fetchFirst();

    return fetchOne != null;
  }

  @Override
  public boolean canApplyDeal(Long userId, Long productId) {
    Integer fetchOne = jpaQueryFactory
        .selectOne()
        .from(product)
        .where(
            product.id.eq(productId),
            product.status.in(CAN_APPLY_DEAL_PRODUCT_STATUS),
            product.consignment.isFalse(),
            product.sellerId.ne(userId)
        )
        .fetchFirst();

    if (fetchOne == null) {
      fetchOne = jpaQueryFactory
          .selectOne()
          .from(product)
          .leftJoin(consignment).on(product.id.eq(consignment.productId))
          .where(
              product.id.eq(productId),
              product.sellerId.ne(userId),
              product.status.in(CAN_APPLY_DEAL_PRODUCT_STATUS),
              product.consignment.isTrue(),
              consignment.adminId.ne(userId)
          )
          .fetchFirst();
    }

    return fetchOne != null;
  }

  @Override
  public boolean isSeller(Long userId, Long productId) {
    Integer fetchOne = jpaQueryFactory
        .selectOne()
        .from(product)
        .where(
            product.id.eq(productId),
            product.consignment.isFalse(),
            product.sellerId.eq(userId)
        )
        .fetchFirst();

    if (fetchOne == null) {
      fetchOne = jpaQueryFactory
          .selectOne()
          .from(product)
          .leftJoin(consignment).on(product.id.eq(consignment.productId))
          .where(
              product.id.eq(productId),
              product.consignment.isTrue(),
              consignment.adminId.eq(userId)
          )
          .fetchFirst();
    }

    return fetchOne != null;
  }

  @Override
  public Optional<Product> findByDealMethodId(Long detailId) {
    return Optional.ofNullable(jpaQueryFactory
        .select(product)
        .from(product)
        .leftJoin(dealMethod).on(dealMethod.productId.eq(product.id))
        .where(
            dealMethod.id.eq(detailId)
        )
        .fetchFirst());
  }
}
