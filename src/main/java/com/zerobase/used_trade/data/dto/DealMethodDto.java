package com.zerobase.used_trade.data.dto;

import com.zerobase.used_trade.annotation.EntityId;
import com.zerobase.used_trade.annotation.MeetingTime;
import com.zerobase.used_trade.annotation.Number;
import com.zerobase.used_trade.data.constant.DealMethodType;
import com.zerobase.used_trade.data.domain.Consignment;
import com.zerobase.used_trade.data.domain.DealMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

public class DealMethodDto {
  @Data
  @Builder
  public static class Principle {
    private Long id;
    private Long productId;
    private DealMethodType type;
    private Long additionalPrice;
    private final String location;
    private final LocalDateTime dateTime;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static Principle fromEntity(DealMethod dealMethod) {
      if (dealMethod.getType() == DealMethodType.MEETING) {
        return Principle.builder()
            .id(dealMethod.getId())
            .productId(dealMethod.getProductId())
            .type(dealMethod.getType())
            .additionalPrice(dealMethod.getAdditionalPrice())
            .location(dealMethod.getLocation())
            .dateTime(dealMethod.getDateTime())
            .createdAt(dealMethod.getCreatedAt())
            .updatedAt(dealMethod.getUpdatedAt())
            .build();
      }

      return Principle.builder()
          .id(dealMethod.getId())
          .productId(dealMethod.getProductId())
          .type(dealMethod.getType())
          .additionalPrice(dealMethod.getAdditionalPrice())
          .createdAt(dealMethod.getCreatedAt())
          .updatedAt(dealMethod.getUpdatedAt())
          .build();
    }
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ProductDetailMethod {
    private Long productId;
    private ParcelMethod parcel;
    private ConvenienceMethod convenience;
    private List<MeetingMethod> meeting= new ArrayList<>();

    public ProductDetailMethod(Long productId) {
      this.productId = productId;
    }
  }

  @Data
  @Builder
  public static class ParcelMethod {
    private Long id;
    private Long additionalPrice;

    public static ParcelMethod fromEntity(DealMethod dealMethod) {
      return ParcelMethod.builder()
          .id(dealMethod.getId())
          .additionalPrice(dealMethod.getAdditionalPrice())
          .build();
    }
  }

  @Data
  @Builder
  public static class ConvenienceMethod {
    private Long id;
    private Long additionalPrice;

    public static ConvenienceMethod formEntity(DealMethod dealMethod) {
      return ConvenienceMethod.builder()
          .id(dealMethod.getId())
          .additionalPrice(dealMethod.getAdditionalPrice())
          .build();
    }
  }

  @Data
  @Builder
  public static class MeetingMethod {
    private Long id;
    private Long additionalPrice;
    private String location;
    private LocalDateTime dateTime;

    public static MeetingMethod fromEntity(DealMethod dealMethod) {
      return MeetingMethod.builder()
          .id(dealMethod.getId())
          .additionalPrice(dealMethod.getAdditionalPrice())
          .location(dealMethod.getLocation())
          .dateTime(dealMethod.getDateTime())
          .build();
    }

    public static MeetingMethod fromEntity(Principle dealMethod) {
      return MeetingMethod.builder()
          .id(dealMethod.getId())
          .additionalPrice(dealMethod.getAdditionalPrice())
          .location(dealMethod.getLocation())
          .dateTime(dealMethod.getDateTime())
          .build();
    }
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
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

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class EnrollRequest {
    @NotNull(message = "{validation.Entity.id.NotNull}")
    @EntityId
    private Long productId;

    @Valid
    private EnrollParcel parcel= null;

    @Valid
    private EnrollConvenience convenience= null;

    @Valid
    private Set<EnrollMeeting> meetings = new HashSet<>();
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class EnrollParcel {
    private boolean flag= false;

    @Min(value= 0L, message = "{validation.DealMethod.addtionalPrice.Min}")
    private Long additionalPrice;

    public DealMethod toEntity(Long productId) {
      return DealMethod.builder()
          .productId(productId)
          .type(DealMethodType.PARCEL)
          .additionalPrice(this.additionalPrice)
          .build();
    }
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class EnrollConvenience {
    private boolean flag= false;

    @Min(value= 0L, message = "{validation.DealMethod.addtionalPrice.Min}")
    private Long additionalPrice;

    public DealMethod toEntity(Long productId) {
      return DealMethod.builder()
          .productId(productId)
          .type(DealMethodType.CONVENIENCE)
          .additionalPrice(this.additionalPrice)
          .build();
    }
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @EqualsAndHashCode(of = {"location", "dateTime"})
  public static class EnrollMeeting {
    @Min(value= 0L, message = "{validation.DealMethod.addtionalPrice.Min}")
    private Long additionalPrice;

    @NotBlank(message = "{validation.DealMethod.Meeting.location.NotBlank}")
    private String location;

    @NotBlank(message = "{validation.DealMethod.Meeting.dateTime.NotBlank}")
    @MeetingTime
    private String dateTime;

    public DealMethod toEntity(Long productId, LocalDateTime dateTime) {
      return DealMethod.builder()
          .productId(productId)
          .type(DealMethodType.MEETING)
          .additionalPrice(this.additionalPrice)
          .location(this.location)
          .dateTime(dateTime)
          .build();
    }
  }
}