package com.zerobase.used_trade.repository;

import com.zerobase.used_trade.data.domain.Deal;
import com.zerobase.used_trade.repository.custom.CustomDealRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DealRepository extends JpaRepository<Deal, Long>, CustomDealRepository {

}
