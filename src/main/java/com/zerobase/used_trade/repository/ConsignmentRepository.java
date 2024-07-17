package com.zerobase.used_trade.repository;

import com.zerobase.used_trade.data.domain.Consignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsignmentRepository extends JpaRepository<Consignment, Long> {

}
