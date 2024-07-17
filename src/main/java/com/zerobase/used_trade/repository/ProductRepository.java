package com.zerobase.used_trade.repository;

import com.zerobase.used_trade.data.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
