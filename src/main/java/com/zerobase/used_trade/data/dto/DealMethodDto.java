package com.zerobase.used_trade.data.dto;

import com.zerobase.used_trade.annotation.Number;
import com.zerobase.used_trade.data.domain.Consignment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

public class DealMethodDto {
  @Data
  @Builder
  public static class ParcelInfo {
    @NotBlank(message = "{validation.Parcel.parcelCompany.NotBlank}")
    private String parcelCompany;

    @NotEmpty(message = "{validation.Parcel.invoiceNumber.NotEmpty}")
    @Number
    private String invoiceNumber;

    public static ParcelInfo fromEntity(Consignment consignment, boolean returnInfo) {
      if (returnInfo) {
        return ParcelInfo.builder()
            .parcelCompany(consignment.getReturnParcelCompany())
            .invoiceNumber(consignment.getReturnInvoiceNumber())
            .build();
      }

      return ParcelInfo.builder()
          .parcelCompany(consignment.getParcelCompany())
          .invoiceNumber(consignment.getParcelCompany())
          .build();
    }
  }
}
