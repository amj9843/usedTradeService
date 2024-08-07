package com.zerobase.used_trade.repository;

import com.zerobase.used_trade.data.domain.Product;
import com.zerobase.used_trade.repository.custom.CustomProductRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, CustomProductRepository {

}
