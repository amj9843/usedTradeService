package com.zerobase.used_trade.data.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "address")
@Entity
public class Address {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "address_id")
  private Long id;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "zip_code")
  private String zipCode;

  @Column(name = "road_address")
  private String roadAddress;

  @Column(name = "address")
  private String address;

  @Column(name = "detail")
  private String detail;

  @Column(name = "representative")
  private boolean representative;

  public void update(String zipCode, String roadAddress, String address, String detail, boolean representative) {
    this.zipCode = zipCode;
    this.roadAddress = roadAddress;
    this.address = address;
    this.detail = detail;
    this.representative = representative;
  }

  public void changeRepresentative() {
    this.representative = !this.representative;
  }

  @Builder
  public Address(
      Long userId, String zipCode, String roadAddress, String address, String detail, boolean representative) {
    this.userId = userId;
    this.zipCode = zipCode;
    this.roadAddress = roadAddress;
    this.address = address;
    this.detail = detail;
    this.representative = representative;
  }
}
