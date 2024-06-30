package com.zerobase.used_trade.data.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product_match_category", uniqueConstraints = {
    @UniqueConstraint(
        columnNames = {"product_id", "category_id"}
    )
})
@Entity
public class ProductMatchCategory extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "match_id")
  private Long id;

  @Column(name = "product_id")
  private Long productId;

  @Column(name = "category_id")
  private Long categoryId;

  @Builder
  public ProductMatchCategory(Long productId, Long categoryId) {
    this.productId = productId;
    this.categoryId = categoryId;
  }
}
