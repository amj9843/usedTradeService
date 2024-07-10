package com.zerobase.used_trade.repository.custom.impl;

import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.used_trade.data.domain.QDomain;
import com.zerobase.used_trade.data.domain.QReview;
import com.zerobase.used_trade.data.domain.QUser;
import com.zerobase.used_trade.data.dto.QUserDto_Employee;
import com.zerobase.used_trade.data.dto.UserDto.Employee;
import com.zerobase.used_trade.repository.custom.CustomUserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomUserRepositoryImpl implements CustomUserRepository {
  private final JPAQueryFactory jpaQueryFactory;
  private final QUser user = QUser.user;
  private final QDomain domain = QDomain.domain;
  private final QReview review = QReview.review;

  @Override
  public List<Employee> findEmployeeByDomainId(Long domainId) {
    return jpaQueryFactory
        .select(
            new QUserDto_Employee(
                user.id, user.email, user.name, user.nickName, user.phoneNumber,
                new CaseBuilder()
                    .when(review.dealerScore.sum().add(50).add(
                        review.dealerScore.count().multiply(-3))
                        .coalesce(50).gt(100))
                    .then(100)
                    .when(review.dealerScore.sum().add(50).add(
                        review.dealerScore.count().multiply(-3))
                        .coalesce(50).lt(0))
                    .then(0)
                    .otherwise(
                        review.dealerScore.sum().add(50).add(
                        review.dealerScore.count().multiply(-3))
                            .coalesce(50)).as("sellScore"))
        ).from(user)
        .leftJoin(domain).on(user.domainId.eq(domain.id))
        .leftJoin(review).on(review.dealerId.eq(user.id))
        .groupBy(user.id)
        .fetch();
  }
}
