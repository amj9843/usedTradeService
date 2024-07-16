package com.zerobase.used_trade.data.domain;

import com.zerobase.used_trade.data.constant.ProductStatus;
import com.zerobase.used_trade.data.constant.Quality;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "product")
@Entity
public class Product extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "product_id")
  private Long id;

  @Column(name = "seller_id")
  private Long sellerId;

  @Column(name = "name")
  private String name;

  @Column(name = "quality")
  @Enumerated(EnumType.STRING)
  private Quality quality;

  @Column(name = "price")
  private Long price;

  @Column(name = "description")
  private String description;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private ProductStatus status;

  @Column(name = "consignment")
  private boolean consignment;

  public void updateStatus(ProductStatus status) {
    this.status = status;
  }

  @Builder
  public Product(
      Long sellerId, String name, Quality quality, Long price, String description,
      ProductStatus status, boolean consignment) {
    this.sellerId = sellerId;
    this.name = name;
    this.quality = quality;
    this.price = price;
    this.description = description;
    this.status = status;
    this.consignment = consignment;
  }
}
