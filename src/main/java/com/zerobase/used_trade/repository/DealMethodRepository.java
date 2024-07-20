package com.zerobase.used_trade.repository;

import com.zerobase.used_trade.data.domain.DealMethod;
import com.zerobase.used_trade.repository.custom.CustomDealMethodRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DealMethodRepository extends JpaRepository<DealMethod, Long>, CustomDealMethodRepository {

}
