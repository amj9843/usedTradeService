package com.zerobase.used_trade.repository;

import com.zerobase.used_trade.data.domain.Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DomainRepository extends JpaRepository<Domain, Long>, JpaSpecificationExecutor<Domain> {
  //도메인 주소 중복값 확인
  boolean existsByDomainAddress(String domainAddress);
}
