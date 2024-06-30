package com.zerobase.used_trade.data.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "domain")
@Entity
public class Domain {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "domain_id")
  private Long id;

  @Column(name = "domain")
  private String domain;

  @Column(name = "company_name")
  private String companyName;

  @Column(name = "business_number")
  private String businessNumber;

  @Column(name = "zip_code")
  private String zipCode;

  @Column(name = "road_address")
  private String roadAddress;

  @Column(name = "address")
  private String address;

  @Column(name = "detail")
  private String detail;

  @Column(name = "phone_Number")
  private String phoneNumber;

  @Column(name = "end_at")
  private LocalDateTime endAt;

  public void update(
      String domain, String companyName, String businessNumber,
      String zipCode, String roadAddress, String address, String detail,
      String phoneNumber, LocalDateTime endAt) {
    this.domain = domain;
    this.companyName = companyName;
    this.businessNumber = businessNumber;
    this.zipCode = zipCode;
    this.roadAddress = roadAddress;
    this.address = address;
    this.detail = detail;
    this.phoneNumber = phoneNumber;
    this.endAt = endAt;
  }

  @Builder
  public Domain(String domain, String companyName, String businessNumber,
      String zipCode, String roadAddress, String address, String detail,
      String phoneNumber, LocalDateTime endAt) {
    this.domain = domain;
    this.companyName = companyName;
    this.businessNumber = businessNumber;
    this.zipCode = zipCode;
    this.roadAddress = roadAddress;
    this.address = address;
    this.detail = detail;
    this.phoneNumber = phoneNumber;
    this.endAt = endAt;
  }
}
