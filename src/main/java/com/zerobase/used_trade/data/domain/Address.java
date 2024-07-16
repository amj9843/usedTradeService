package com.zerobase.used_trade.data.domain;

import com.zerobase.used_trade.data.dto.AddressDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "address", uniqueConstraints = {
    @UniqueConstraint(
        columnNames = {"user_id", "name", "zip_code", "road_address", "common_address", "detail"}
    )
})
@Entity
public class Address extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "address_id")
  private Long id;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "name")
  private String name;

  @Column(name = "zip_code")
  private String zipCode;

  @Column(name = "road_address")
  private String roadAddress;

  @Column(name = "common_address")
  private String commonAddress;

  @Column(name = "detail")
  private String detail;

  @Column(name = "representative")
  private boolean representative;

  public void update(AddressDto.UpdateRequest request) {
    if (request.getName() != null && !request.getName().isBlank()) {
      this.name = request.getName().trim();
    }
    if (request.getZipCode() != null && !request.getZipCode().isBlank()) {
      this.zipCode = request.getZipCode();
    }
    if (request.getRoadAddress() != null && !request.getRoadAddress().isBlank()) {
      this.roadAddress = request.getRoadAddress();
    }
    if (request.getCommonAddress() != null && !request.getCommonAddress().isBlank()) {
      this.commonAddress = request.getCommonAddress();
    }
    if (request.getDetail() != null) {
      this.detail = request.getDetail();
    }
    if (request.isRepresentative()) {
      this.representative = true;
    }
  }

  public void changeRepresentative() {
    this.representative = !this.representative;
  }

  @Builder
  public Address(
      Long userId, String name, String zipCode, String roadAddress, String commonAddress, String detail, boolean representative) {
    this.userId = userId;
    this.name = name;
    this.zipCode = zipCode;
    this.roadAddress = roadAddress;
    this.commonAddress = commonAddress;
    this.detail = detail;
    this.representative = representative;
  }
}
