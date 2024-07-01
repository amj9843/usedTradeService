package com.zerobase.used_trade.data.domain;

import com.zerobase.used_trade.data.constant.DealMethodType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "deal_method")
@Entity
public class DealMethod {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "detail_id")
  private Long id;

  @Column(name = "product_id")
  private Long productId;

  @Column(name = "type")
  @Enumerated(EnumType.STRING)
  private DealMethodType type;

  @Column(name = "additional_price")
  private Long additionalPrice;

  @Column(name = "location")
  private String location;

  @Column(name = "datetime")
  private LocalDateTime dateTime;

  @Builder
  public DealMethod(
      Long productId, DealMethodType type, Long additionalPrice,
      String location, LocalDateTime dateTime) {
    this.productId = productId;
    this.type = type;
    this.additionalPrice = additionalPrice;
    this.location = location;
    this.dateTime = dateTime;
  }
}
