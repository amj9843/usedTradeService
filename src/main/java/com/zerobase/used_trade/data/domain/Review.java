package com.zerobase.used_trade.data.domain;

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
@Table(name = "review")
@Entity
public class Review extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "review_id")
  private Long id;

  @Column(name = "product_id", unique = true)
  private Long productId;

  @Column(name = "buyer_id")
  private Long buyerId;

  @Column(name = "dealer_id")
  private Long dealerId;

  @Column(name = "buyer_score")
  private int buyerScore;

  @Column(name = "dealer_score")
  private int dealerScore;

  @Column(name = "quality")
  @Enumerated(EnumType.STRING)
  private Quality quality;

  @Column(name = "content")
  private String content;

  public void updateBuyerScore(int buyerScore) {
    this.buyerScore = buyerScore;
  }

  public void update(int dealerScore, Quality quality, String content) {
    this.dealerScore = dealerScore;
    this.quality = quality;
    this.content = content;
  }

  @Builder
  public Review(Long productId, Long buyerId, Long dealerId, int buyerScore, int dealerScore, Quality quality, String content) {
    this.productId = productId;
    this.buyerId = buyerId;
    this.dealerId = dealerId;
    this.buyerScore = buyerScore;
    this.dealerScore = dealerScore;
    this.quality = quality;
    this.content = content;
  }
}
