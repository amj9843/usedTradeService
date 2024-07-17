package com.zerobase.used_trade.service;

import com.zerobase.used_trade.data.constant.CategorySortType;
import com.zerobase.used_trade.data.dto.CategoryDto.EnrollRequest;
import com.zerobase.used_trade.data.dto.CategoryDto.Principle;
import com.zerobase.used_trade.data.dto.CategoryDto.UpdateRequest;
import java.util.Set;
import org.springframework.data.domain.Page;

public interface CategoryService {
  Principle enrollCategory(Long userId, EnrollRequest request);

  Page<Principle> getCategoryList(int page, int size, CategorySortType criteria);

  Principle getCategory(Long categoryId);

  void updateCategoryInfo(Long userId, Long categoryId, UpdateRequest request);

  void deleteCategory(Long userId, Long categoryId);

  void enrollProductsOnCategory(Long userId, Long categoryId, Set<Long> productIdList);

  void deleteProductsOnCategory(Long userId, Long categoryId, Set<Long> productIdList);
}
