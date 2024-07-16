package com.zerobase.used_trade.data.dto;

import com.zerobase.used_trade.annotation.EmptyOrNotBlank;
import com.zerobase.used_trade.annotation.ShortString;
import com.zerobase.used_trade.annotation.ZipCode;
import com.zerobase.used_trade.data.domain.Address;
import com.zerobase.used_trade.data.domain.Consignment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AddressDto {
  @Data
  @Builder
  public static class Principle {
    private Long id;
    private Long userId;
    private String name;
    private String zipCode;
    private String roadAddress;
    private String commonAddress;
    private String detail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean representative;

    public static Principle fromEntity(Address address) {
      return Principle.builder()
          .id(address.getId())
          .userId(address.getUserId())
          .name(address.getName())
          .zipCode(address.getZipCode())
          .roadAddress(address.getRoadAddress())
          .commonAddress(address.getCommonAddress())
          .detail(address.getDetail())
          .createdAt(address.getCreatedAt())
          .updatedAt(address.getUpdatedAt())
          .representative(address.isRepresentative())
          .build();
    }
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class EnrollRequest {
    @NotBlank(message = "{validation.Address.name.NotBlank}")
    @ShortString
    private String name;

    @NotEmpty(message = "{validation.Address.zipCode.NotEmpty}")
    @ZipCode
    private String zipCode;

    @NotBlank(message = "{validation.Address.roadAddress.NotBlank}")
    private String roadAddress;

    @NotBlank(message = "{validation.Address.address.NotBlank}")
    private String commonAddress;

    @EmptyOrNotBlank
    private String detail;

    private boolean representative= false;

    public Address toEntity(Long userId) {
      return Address.builder()
          .userId(userId)
          .name(this.name.trim())
          .zipCode(this.zipCode)
          .roadAddress(this.roadAddress)
          .commonAddress(this.commonAddress)
          .detail(this.detail)
          .representative(this.representative)
          .build();
    }
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UpdateRequest{
    @EmptyOrNotBlank
    @ShortString
    private String name;

    @ZipCode
    private String zipCode;

    @EmptyOrNotBlank
    private String roadAddress;

    @EmptyOrNotBlank
    private String commonAddress;

    @EmptyOrNotBlank
    private String detail;

    private boolean representative= false;
  }

  @Data
  @Builder
  public static class AddressInfo {
    private String name;
    private String zipCode;
    private String roadAddress;
    private String address;
    private String detail;

    public static AddressInfo fromEntity(Consignment consignment) {
      return AddressInfo.builder()
          .name(consignment.getSellerName())
          .zipCode(consignment.getZipCode())
          .roadAddress(consignment.getRoadAddress())
          .address(consignment.getAddress())
          .detail(consignment.getAddressDetail())
          .build();
    }

    public static AddressInfo fromEntity(Address address) {
      return AddressInfo.builder()
          .name(address.getName())
          .zipCode(address.getZipCode())
          .roadAddress(address.getRoadAddress())
          .address(address.getCommonAddress())
          .detail(address.getDetail())
          .build();
    }
  }
}
