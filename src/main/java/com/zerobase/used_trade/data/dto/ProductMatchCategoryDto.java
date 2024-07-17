package com.zerobase.used_trade.data.dto;

import com.zerobase.used_trade.annotation.EntityId;
import com.zerobase.used_trade.data.domain.ProductMatchCategory;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

public class ProductMatchCategoryDto {
  @Data
  @Builder
  public static class EnrollRequest {
    @NotNull(message = "{validation.Entity.id.NotNull}")
    @EntityId
    private Long productId;

    @NotNull(message = "{validation.Entity.id.NotNull}")
    @EntityId
    private Long categoryId;

    public static ProductMatchCategory toEntity(Long productId, Long categoryId) {
      return ProductMatchCategory.builder()
          .productId(productId)
          .categoryId(categoryId)
          .build();
    }
  }
}
