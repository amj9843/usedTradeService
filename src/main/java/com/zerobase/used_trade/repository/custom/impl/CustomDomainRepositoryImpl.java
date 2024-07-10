package com.zerobase.used_trade.repository.custom.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.used_trade.data.domain.QDomain;
import com.zerobase.used_trade.repository.custom.CustomDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomDomainRepositoryImpl implements CustomDomainRepository {
  private final JPAQueryFactory jpaQueryFactory;
  private final QDomain domain = QDomain.domain;
  
  @Override
  public Long findIdByDomainAddress(String domainAddress) {
    return jpaQueryFactory.select(domain.id)
        .from(domain)
        .where(domain.domainAddress.eq(domainAddress))
        .fetchFirst();
  }
}
