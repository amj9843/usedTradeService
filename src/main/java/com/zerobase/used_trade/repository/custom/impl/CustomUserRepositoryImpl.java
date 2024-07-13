package com.zerobase.used_trade.repository.custom.impl;

import static com.zerobase.used_trade.util.ScoreUtility.BASIC_REVIEW_SCORE;
import static com.zerobase.used_trade.util.ScoreUtility.BASIC_SCORE;
import static com.zerobase.used_trade.util.ScoreUtility.HIGHEST_SCORE;
import static com.zerobase.used_trade.util.ScoreUtility.LOWEST_SCORE;

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
                /* 검색한 도메인을 메일 주소로 이용하고 있는 사용자의 '판매 점수' 계산
                 *
                 * 최종 점수는 기본점수에 review 에서 평가받은 점수를 토대로 산출,
                 * 중간점수인 3을 기준으로 각 리뷰에서 받은 점수의 차를 기본 점수에서 계산
                 * 최소/최대 범위를 넘어갈 경우 값 고정
                 */
                new CaseBuilder()
                    .when(review.dealerScore.sum().add(BASIC_SCORE).add(
                        review.dealerScore.count().multiply(-1 * BASIC_REVIEW_SCORE))
                        .coalesce(BASIC_SCORE).gt(HIGHEST_SCORE))
                    .then(HIGHEST_SCORE)
                    .when(review.dealerScore.sum().add(BASIC_SCORE).add(
                        review.dealerScore.count().multiply(-1 * BASIC_REVIEW_SCORE))
                        .coalesce(BASIC_SCORE).lt(LOWEST_SCORE))
                    .then(LOWEST_SCORE)
                    .otherwise(
                        review.dealerScore.sum().add(BASIC_SCORE).add(
                        review.dealerScore.count().multiply(-1 * BASIC_REVIEW_SCORE))
                            .coalesce(BASIC_SCORE)).as("sellScore"))
        ).from(user)
        .leftJoin(domain).on(user.domainId.eq(domain.id))
        .leftJoin(review).on(review.dealerId.eq(user.id))
        .groupBy(user.id)
        .fetch();
  }
}
