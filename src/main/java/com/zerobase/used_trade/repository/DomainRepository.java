package com.zerobase.used_trade.repository;

import com.zerobase.used_trade.data.domain.Domain;
import com.zerobase.used_trade.repository.custom.CustomDomainRepository;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DomainRepository extends JpaRepository<Domain, Long>, JpaSpecificationExecutor<Domain>,
    CustomDomainRepository {
  //유효성 확인
  boolean existsByIdAndEndAtAfter(Long id, LocalDateTime time);
}
