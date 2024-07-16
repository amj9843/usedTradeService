package com.zerobase.used_trade.repository;

import com.zerobase.used_trade.data.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
