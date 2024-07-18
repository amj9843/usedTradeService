package com.zerobase.used_trade.data.dto;

import com.zerobase.used_trade.data.domain.Consignment;
import com.zerobase.used_trade.data.dto.AccountDto.AccountInfo;
import com.zerobase.used_trade.data.dto.AddressDto.AddressInfo;
import com.zerobase.used_trade.data.dto.DealMethodDto.ParcelInfo;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

public class ConsignmentDto {
  @Data
  @Builder
  public static class Principle {
    private Long id;
    private Long productId;
    private AddressInfo addressInfo;
    private AccountInfo accountInfo;
    private Long adminId;
    private ParcelInfo parcelInfo;
    private ParcelInfo returnParcelInfo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Principle fromEntity(Consignment consignment) {
      return Principle.builder()
          .id(consignment.getId())
          .productId(consignment.getProductId())
          .addressInfo(AddressInfo.fromEntity(consignment))
          .accountInfo(AccountInfo.fromEntity(consignment))
          .adminId(consignment.getAdminId())
          .parcelInfo(ParcelInfo.fromEntity(consignment, false))
          .returnParcelInfo(ParcelInfo.fromEntity(consignment, true))
          .createdAt(consignment.getCreatedAt())
          .updatedAt(consignment.getUpdatedAt())
          .build();
    }
  }
}
