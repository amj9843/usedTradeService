package com.zerobase.used_trade.data.dto;

import com.zerobase.used_trade.annotation.EmptyOrNotBlank;
import com.zerobase.used_trade.annotation.EntityId;
import com.zerobase.used_trade.annotation.ValidEnum;
import com.zerobase.used_trade.data.constant.ProductStatus;
import com.zerobase.used_trade.data.constant.Quality;
import com.zerobase.used_trade.data.domain.Account;
import com.zerobase.used_trade.data.domain.Address;
import com.zerobase.used_trade.data.domain.Consignment;
import com.zerobase.used_trade.data.domain.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ProductDto {
  @Data
  @Builder
  public static class Principle {
    private Long id;
    private List<ImageDto.Principle> images;
    private Set<CategoryDto.Principle> categories;
    private Long sellerId;
    private String name;
    private Quality quality;
    private Long price;
    private String description;
    private ProductStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean consignment;

    public static Principle fromEntity(Product product,
        List<ImageDto.Principle> images, Set<CategoryDto.Principle> categories) {
      return Principle.builder()
          .id(product.getId())
          .images(images)
          .categories(categories)
          .sellerId(product.getSellerId())
          .name(product.getName())
          .quality(product.getQuality())
          .price(product.getPrice())
          .description(product.getDescription())
          .status(product.getStatus())
          .consignment(product.isConsignment())
          .createdAt(product.getCreatedAt())
          .updatedAt(product.getUpdatedAt())
          .build();
    }
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class EnrollDirectRequest {
    @NotBlank(message = "{validation.Product.name.NotBlank}")
    private String name;

    @NotNull(message = "{validation.Product.price.NotNull}")
    @Min(value= 0L, message = "{validation.Product.price.Min}")
    private Long price;

    @EmptyOrNotBlank
    private String description;

    @NotEmpty(message = "{validation.Product.quality.NotEmpty}")
    @ValidEnum(enumClass = Quality.class)
    private String quality;

    private Set<Long> category= new HashSet<>();

    public Product toEntity(Long sellerId) {
      return Product.builder()
          .sellerId(sellerId)
          .name(this.name)
          .quality(Quality.valueOf(this.quality))
          .price(this.price)
          .description(this.description)
          .status(ProductStatus.SELLING)
          .consignment(false)
          .build();
    }
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class EnrollConsignmentRequest {
    @NotBlank(message = "{validation.Product.name.NotBlank}")
    private String name;

    @NotNull(message = "{validation.Product.price.NotNull}")
    @Min(value= 0L, message = "{validation.Product.price.Min}")
    private Long price;

    @EmptyOrNotBlank
    private String description;

    @EntityId
    private Long accountId;

    @EntityId
    private Long addressId;

    private Set<Long> category= new HashSet<>();

    public Product toEntity(Long sellerId) {
      return Product.builder()
          .sellerId(sellerId)
          .name(this.name)
          .price(this.price)
          .description(this.description)
          .status(ProductStatus.CONSIGNMENT_APPLY)
          .consignment(true)
          .build();
    }

    public Consignment toEntity(Long productId, Address address, Account account) {
      return Consignment.builder()
          .productId(productId)
          .sellerName(address.getName())
          .zipCode(address.getZipCode())
          .roadAddress(address.getRoadAddress())
          .address(address.getCommonAddress())
          .addressDetail(address.getDetail())
          .bank(account.getBank())
          .accountNumber(account.getAccountNumber())
          .accountOwnerName(account.getOwnerName())
          .build();
    }
  }

  @Data
  @Builder
  public static class EnrollConsignmentResponse {
    private Principle productInfo;
    private ConsignmentDto.Principle consignmentInfo;

    public static EnrollConsignmentResponse fromEntity(Product product, Consignment consignment,
        List<ImageDto.Principle> images, Set<CategoryDto.Principle> categories) {
      return EnrollConsignmentResponse.builder()
          .productInfo(
              Principle.fromEntity(
                  product, images, categories))
          .consignmentInfo(
              ConsignmentDto.Principle.fromEntity(consignment)
          ).build();
    }
  }
}
