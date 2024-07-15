package com.zerobase.used_trade.data.dto;

import com.zerobase.used_trade.annotation.BusinessNumber;
import com.zerobase.used_trade.annotation.DomainAddress;
import com.zerobase.used_trade.annotation.EmptyOrNotBlank;
import com.zerobase.used_trade.annotation.PhoneNumber;
import com.zerobase.used_trade.annotation.ValidEnum;
import com.zerobase.used_trade.annotation.ZipCode;
import com.zerobase.used_trade.data.constant.SubscribeType;
import com.zerobase.used_trade.data.domain.Domain;
import com.zerobase.used_trade.data.dto.UserDto.Employee;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

public class DomainDto {
  @Data
  @Builder
  public static class Principle {
    private Long id;
    private String domainAddress;
    private String companyName;
    private String businessNumber;
    private String zipCode;
    private String roadAddress;
    private String address;
    private String detail;
    private String phoneNumber;
    private LocalDateTime endAt;

    public static Principle fromEntity(Domain domain) {
      return Principle.builder()
          .id(domain.getId())
          .domainAddress(domain.getDomainAddress())
          .companyName(domain.getCompanyName())
          .businessNumber(domain.getBusinessNumber())
          .zipCode(domain.getZipCode())
          .roadAddress(domain.getRoadAddress())
          .address(domain.getAddress())
          .detail(domain.getDetail())
          .phoneNumber(domain.getPhoneNumber())
          .endAt(domain.getEndAt())
          .build();
    }
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class EnrollRequest {
    @NotEmpty(message = "{validation.Domain.address.NotEmpty}")
    @DomainAddress
    private String domainAddress;

    @NotBlank(message = "{validation.Company.name.NotBlank}")
    private String companyName;

    @NotEmpty(message = "{validation.Company.businessNumber.NotEmpty}")
    @BusinessNumber
    private String businessNumber;

    @NotEmpty(message = "{validation.Address.zipCode.NotEmpty}")
    @ZipCode
    private String zipCode;

    @NotBlank(message = "{validation.Address.roadAddress.NotBlank}")
    private String roadAddress;

    @NotBlank(message = "{validation.Address.address.NotBlank}")
    private String address;

    @EmptyOrNotBlank
    private String detail;

    @NotEmpty(message = "{validation.Company.phoneNumber.NotEmpty}")
    @PhoneNumber
    private String phoneNumber;

    @NotEmpty(message = "{validation.Domain.period.NotEmpty}")
    @ValidEnum(enumClass = SubscribeType.class)
    private String period;

    public Domain toEntity() {
      LocalDateTime endAt =
          SubscribeType.valueOf(this.period).extension(LocalDateTime.now());

      return Domain.builder()
          .domainAddress(this.domainAddress)
          .companyName(this.companyName)
          .businessNumber(this.businessNumber)
          .zipCode(this.zipCode)
          .roadAddress(this.roadAddress)
          .address(this.address)
          .detail(this.detail)
          .phoneNumber(this.phoneNumber)
          .endAt(endAt)
          .build();
    }
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ExtensionRequest {
    @NotEmpty(message = "{validation.Domain.period.NotEmpty}")
    @ValidEnum(enumClass = SubscribeType.class)
    private String period;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UpdateRequest{
    @DomainAddress
    private String domainAddress;

    @EmptyOrNotBlank
    private String companyName;

    @BusinessNumber
    private String businessNumber;

    @ZipCode
    private String zipCode;

    @EmptyOrNotBlank
    private String roadAddress;

    @EmptyOrNotBlank
    private String address;

    @EmptyOrNotBlank
    private String detail;

    @PhoneNumber
    private String phoneNumber;
  }

  @Data
  @NoArgsConstructor
  public static class DetailInfoResponse{
    private String domainAddress;
    private boolean valid;
    private CompanyInfo companyInformation;
    private Page<Employee> employees;

    public DetailInfoResponse(Domain domain, Page<Employee> employees) {
      this.domainAddress = domain.getDomainAddress();
      this.valid = domain.getEndAt().isAfter(LocalDateTime.now());
      this.companyInformation = CompanyInfo.builder()
          .name(domain.getCompanyName())
          .businessNumber(domain.getBusinessNumber())
          .zipCode(domain.getZipCode())
          .roadAddress(domain.getRoadAddress())
          .address(domain.getAddress())
          .detail(domain.getDetail())
          .phoneNumber(domain.getPhoneNumber())
          .build();
      this.employees = employees;
    }
  }

  @Data
  @Builder
  public static class CompanyInfo{
    private String name;
    private String businessNumber;
    private String zipCode;
    private String roadAddress;
    private String address;
    private String detail;
    private String phoneNumber;
  }
}
