package com.zerobase.used_trade.data.dto;

import static java.util.Arrays.asList;

import com.querydsl.core.annotations.QueryProjection;
import com.zerobase.used_trade.annotation.EmptyOrNotBlank;
import com.zerobase.used_trade.annotation.EntityId;
import com.zerobase.used_trade.annotation.Number;
import com.zerobase.used_trade.annotation.PassedTime;
import com.zerobase.used_trade.annotation.PhoneNumber;
import com.zerobase.used_trade.annotation.ShortString;
import com.zerobase.used_trade.data.constant.Bank;
import com.zerobase.used_trade.data.constant.DealMethodType;
import com.zerobase.used_trade.data.constant.DealStatus;
import com.zerobase.used_trade.data.domain.Account;
import com.zerobase.used_trade.data.domain.Address;
import com.zerobase.used_trade.data.domain.Deal;
import com.zerobase.used_trade.data.domain.User;
import com.zerobase.used_trade.data.dto.AccountDto.AccountInfo;
import com.zerobase.used_trade.data.dto.AddressDto.AddressInfo;
import com.zerobase.used_trade.data.dto.DealMethodDto.MeetingMethod;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class DealDto {
  @Data
  @Builder
  public static class Principle {
    private Long id;
    private Long buyerId;
    private Long detailId;
    private DealStatus status;
    private Bank bank;
    private String accountNumber;
    private String accountOwnerName;
    private String depositorName;
    private LocalDateTime depositedAt;
    private Long depositPrice;
    private Bank refundBank;
    private String refundAccountNumber;
    private String refundAccountOwnerName;
    private String buyerZipCode;
    private String buyerRoadAddress;
    private String buyerAddress;
    private String buyerAddressDetail;
    private String buyerName;
    private String buyerPhoneNumber;
    private String dealerName;
    private String convenienceStore;
    private String parcelCompany;
    private String invoiceNumber;

    public static Principle fromEntity(Deal deal) {
      return Principle.builder()
          .id(deal.getId())
          .buyerId(deal.getBuyerId())
          .detailId(deal.getDetailId())
          .status(deal.getStatus())
          .bank(deal.getBank())
          .accountNumber(deal.getAccountNumber())
          .depositorName(deal.getDepositorName())
          .depositedAt(deal.getDepositedAt())
          .depositPrice(deal.getDepositPrice())
          .refundBank(deal.getRefundBank())
          .refundAccountNumber(deal.getRefundAccountNumber())
          .refundAccountOwnerName(deal.getRefundAccountOwnerName())
          .buyerZipCode(deal.getBuyerZipCode())
          .buyerRoadAddress(deal.getBuyerRoadAddress())
          .buyerAddress(deal.getBuyerAddress())
          .buyerAddressDetail(deal.getBuyerAddressDetail())
          .buyerName(deal.getBuyerName())
          .buyerPhoneNumber(deal.getBuyerPhoneNumber())
          .dealerName(deal.getDealerName())
          .convenienceStore(deal.getConvenienceStore())
          .parcelCompany(deal.getParcelCompany())
          .invoiceNumber(deal.getInvoiceNumber())
          .build();
    }
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class EnrollAndUpdateRequest {
    @PhoneNumber
    private String phoneNumber;

    @EntityId
    private Long accountId;

    @EntityId
    private Long addressId;

    @ShortString
    private String name;

    @EmptyOrNotBlank
    private String convenienceStore;

    public Deal toEntity(Long dealMethodId, Long buyerId,
        Account account, Address address) {
      return Deal.builder()
          .buyerId(buyerId)
          .detailId(dealMethodId)
          .status(DealStatus.APPLIED)
          .refundBank((account != null)? account.getBank(): null)
          .refundAccountNumber((account != null)? account.getAccountNumber(): null)
          .refundAccountOwnerName((account != null)? account.getOwnerName(): null)
          .buyerZipCode((address != null)? address.getZipCode(): null)
          .buyerRoadAddress((address != null)? address.getRoadAddress(): null)
          .buyerAddress((address != null)? address.getCommonAddress(): null)
          .buyerAddressDetail((address != null)? address.getDetail(): null)
          .buyerName((address != null)? address.getName(): this.name)
          .buyerPhoneNumber(this.phoneNumber)
          .convenienceStore(this.convenienceStore)
          .build();
    }
  }

  @Data
  @NoArgsConstructor
  public static class SimpleInfoResponse {
    private Long id;
    private LocalDateTime createdAt;
    private String type;
    private Long buyerId;
    private DealStatus status;

    @QueryProjection
    public SimpleInfoResponse(Long dealId, LocalDateTime createdAt, DealMethodType type, Long buyerId, DealStatus status) {
      this.id = dealId;
      this.createdAt = createdAt;
      this.type = type.toString();
      this.buyerId = buyerId;
      this.status = status;
    }
  }

  @Data
  @Builder
  public static class DetailDealInfoOfMeeting {
    private Long id;
    private Long dealerId;
    private Long buyerId;
    private String type;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private PhoneNumbers phoneNumbers;
    private MeetingMethod meetingInfo;

    public static DetailDealInfoOfMeeting fromEntity(Deal deal, DealMethodDto.Principle dealMethod, User dealer) {
      return DetailDealInfoOfMeeting.builder()
          .id(deal.getId())
          .dealerId(dealer.getId())
          .buyerId(deal.getBuyerId())
          .type(dealMethod.getType().toString())
          .status(deal.getStatus().toString())
          .createdAt(deal.getCreatedAt())
          .updatedAt(deal.getUpdatedAt())
          .phoneNumbers(PhoneNumbers.fromEntity(dealer, deal))
          .meetingInfo(MeetingMethod.fromEntity(dealMethod))
          .build();
    }
  }

  @Data
  @Builder
  public static class PhoneNumbers {
    private String dealerPhoneNumber;
    private String buyerPhoneNumber;

    public static PhoneNumbers fromEntity(User dealer, Deal deal) {
      return PhoneNumbers.builder()
          .dealerPhoneNumber(dealer.getPhoneNumber())
          .buyerPhoneNumber(deal.getBuyerPhoneNumber())
          .build();
    }
  }

  @Data
  @Builder
  public static class DetailDealInfoOfParcel {
    private Long id;
    private Long dealerId;
    private Long buyerId;
    private String type;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<AccountDto.Principle> dealerAccounts;
    private DepositInfo depositInfo;
    private AccountInfo refundAccountInfo;
    private ReceivedParcelInfo receivedInfo;
    private ShippingParcelInfo shippingInfo;

    public static DetailDealInfoOfParcel fromEntity(Deal deal, DealMethodDto.Principle dealMethod, User dealer,
        List<AccountDto.Principle> dealerAccounts) {
      AccountInfo accountInfo = new AccountInfo(
          (deal.getBank() == null)? null: deal.getBank().description(),
          deal.getAccountNumber(), deal.getAccountOwnerName());

      AccountInfo refundAccountInfo = null;
      ReceivedParcelInfo receivedParcelInfo = null;
      //거래 상태가 입금 완료, 입금 확인, 배송 진행중(SHIPPING) 상태일때만 값이 보이는 항목들
      if (asList(DealStatus.DEPOSITED, DealStatus.CONFIRMED, DealStatus.SHIPPING).contains(deal.getStatus())) {
        refundAccountInfo = new AccountInfo(
            deal.getRefundBank().description(), deal.getRefundAccountNumber(), deal.getRefundAccountOwnerName());
        receivedParcelInfo = ReceivedParcelInfo.fromEntity(deal);
      }

      return DetailDealInfoOfParcel.builder()
          .id(deal.getId())
          .dealerId(dealer.getId())
          .buyerId(deal.getBuyerId())
          .type(dealMethod.getType().toString())
          .status(deal.getStatus().toString())
          .createdAt(deal.getCreatedAt())
          .updatedAt(deal.getUpdatedAt())
          .dealerAccounts(dealerAccounts)
          .depositInfo(DepositInfo.fromEntity(deal, accountInfo))
          .refundAccountInfo(refundAccountInfo)
          .receivedInfo(receivedParcelInfo)
          .shippingInfo(ShippingParcelInfo.fromEntity(deal))
          .build();
    }
  }

  @Data
  @Builder
  public static class ReceivedParcelInfo {
    private AddressInfo addressInfo;
    private String phoneNumber;

    public static ReceivedParcelInfo fromEntity(Deal deal) {
      AddressInfo addressInfo = new AddressInfo(
          deal.getBuyerName(), deal.getBuyerZipCode(), deal.getBuyerRoadAddress(),
          deal.getBuyerAddress(), deal.getBuyerAddressDetail());

      return ReceivedParcelInfo.builder()
          .addressInfo(addressInfo)
          .phoneNumber(deal.getBuyerPhoneNumber())
          .build();
    }
  }

  @Data
  @Builder
  public static class ReceivedConvenienceInfo {
    private String buyerName;
    private String convenience;
    private String phoneNumber;

    public static ReceivedConvenienceInfo fromEntity(Deal deal) {
      return ReceivedConvenienceInfo.builder()
          .buyerName(deal.getBuyerName())
          .convenience(deal.getConvenienceStore())
          .phoneNumber(deal.getBuyerPhoneNumber())
          .build();
    }
  }

  @Data
  @Builder
  public static class ShippingParcelInfo {
    private String dealerName;
    private String parcelCompany;
    private String invoiceNumber;

    public static ShippingParcelInfo fromEntity(Deal deal) {
      return ShippingParcelInfo.builder()
          .dealerName(deal.getDealerName())
          .parcelCompany(deal.getParcelCompany())
          .invoiceNumber(deal.getInvoiceNumber())
          .build();
    }
  }

  @Data
  @Builder
  public static class ShippingConvenienceInfo {
    private String dealerName;
    private String invoiceNumber;

    public static ShippingConvenienceInfo fromEntity(Deal deal) {
      return ShippingConvenienceInfo.builder()
          .dealerName(deal.getDealerName())
          .invoiceNumber(deal.getInvoiceNumber())
          .build();
    }
  }

  @Data
  @Builder
  public static class DetailDealInfoOfConvenience {
    private Long id;
    private Long dealerId;
    private Long buyerId;
    private String type;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<AccountDto.Principle> dealerAccounts;
    private DepositInfo depositInfo;
    private AccountInfo refundAccountInfo;
    private ReceivedConvenienceInfo receivedInfo;
    private ShippingConvenienceInfo shippingInfo;
    public static DetailDealInfoOfConvenience fromEntity(Deal deal, DealMethodDto.Principle dealMethod, User dealer,
        List<AccountDto.Principle> dealerAccounts) {
      AccountInfo accountInfo = new AccountInfo(
          (deal.getBank() == null) ? null: deal.getBank().description(),
          deal.getAccountNumber(), deal.getAccountOwnerName());

      AccountInfo refundAccountInfo = null;
      ReceivedConvenienceInfo receivedParcelInfo = null;
      //거래 상태가 입금 완료, 입금 확인, 배송 진행중(SHIPPING) 상태일때만 값이 보이는 항목들
      if (asList(DealStatus.DEPOSITED, DealStatus.CONFIRMED, DealStatus.SHIPPING).contains(deal.getStatus())) {
        refundAccountInfo = new AccountInfo(
            deal.getRefundBank().description(), deal.getRefundAccountNumber(), deal.getRefundAccountOwnerName());
        receivedParcelInfo = ReceivedConvenienceInfo.fromEntity(deal);
      }

      return DetailDealInfoOfConvenience.builder()
          .id(deal.getId())
          .dealerId(dealer.getId())
          .buyerId(deal.getBuyerId())
          .type(dealMethod.getType().toString())
          .status(deal.getStatus().toString())
          .createdAt(deal.getCreatedAt())
          .updatedAt(deal.getUpdatedAt())
          .dealerAccounts(dealerAccounts)
          .depositInfo(DepositInfo.fromEntity(deal, accountInfo))
          .refundAccountInfo(refundAccountInfo)
          .receivedInfo(receivedParcelInfo)
          .shippingInfo(ShippingConvenienceInfo.fromEntity(deal))
          .build();
    }
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class UpdateDepositInfoRequest {
    @EntityId
    private Long accountId;

    @EmptyOrNotBlank
    @ShortString
    private String name;

    @EmptyOrNotBlank
    @PassedTime
    private String depositedAt;

    @Min(value = 0L, message = "{validation.Price.Positive}")
    private Long depositPrice = null;
  }

  @Data
  @Builder
  public static class DepositInfo {
    private AccountInfo depositedAccount;
    private String depositorName;
    private LocalDateTime depositedAt;
    private Long depositPrice;

    public static DepositInfo fromEntity(Deal deal, Account account) {
      return DepositInfo.builder()
          .depositedAccount(AccountInfo.fromEntity(account))
          .depositorName(deal.getDepositorName())
          .depositedAt(deal.getDepositedAt())
          .depositPrice(deal.getDepositPrice())
          .build();
    }

    public static DepositInfo fromEntity(Deal deal, AccountInfo account) {
      return DepositInfo.builder()
          .depositedAccount(account)
          .depositorName(deal.getDepositorName())
          .depositedAt(deal.getDepositedAt())
          .depositPrice(deal.getDepositPrice())
          .build();
    }
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class UpdateShippingInfoRequest {
    @EmptyOrNotBlank
    @ShortString
    private String dealerName;

    @EmptyOrNotBlank
    private String parcelCompany;

    @EmptyOrNotBlank
    @Number
    private String invoiceNumber;
  }
}
