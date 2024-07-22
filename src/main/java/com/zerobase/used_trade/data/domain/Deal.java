package com.zerobase.used_trade.data.domain;

import static com.zerobase.used_trade.util.DateTimeUtility.stringToLocalDateTime;

import com.zerobase.used_trade.data.constant.Bank;
import com.zerobase.used_trade.data.constant.DealStatus;
import com.zerobase.used_trade.data.dto.DealDto.EnrollAndUpdateRequest;
import com.zerobase.used_trade.data.dto.DealDto.UpdateDepositInfoRequest;
import com.zerobase.used_trade.data.dto.DealDto.UpdateShippingInfoRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "deal", uniqueConstraints = {
    @UniqueConstraint(
        columnNames = {"buyer_id", "detail_id"}
    )
})
@Entity
public class Deal extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "deal_id")
  private Long id;

  @Column(name = "buyer_id")
  private Long buyerId;

  @Column(name = "detail_id")
  private Long detailId;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private DealStatus status;

  @Column(name = "account_bank")
  @Enumerated(EnumType.STRING)
  private Bank bank;

  @Column(name = "account_number")
  private String accountNumber;

  @Column(name = "account_owner_name")
  private String accountOwnerName;

  @Column(name = "depositor_name")
  private String depositorName;

  @Column(name = "deposited_at")
  private LocalDateTime depositedAt;

  @Column(name = "deposit_price")
  private Long depositPrice;

  @Column(name = "refund_account_bank")
  @Enumerated(EnumType.STRING)
  private Bank refundBank;

  @Column(name = "refund_account_number")
  private String refundAccountNumber;

  @Column(name = "refund_account_owner_name")
  private String refundAccountOwnerName;

  @Column(name = "buyer_zip_code")
  private String buyerZipCode;

  @Column(name = "buyer_road_address")
  private String buyerRoadAddress;

  @Column(name = "buyer_address")
  private String buyerAddress;

  @Column(name = "buyer_address_detail")
  private String buyerAddressDetail;

  @Column(name = "buyer_name")
  private String buyerName;

  @Column(name = "buyer_phone_number")
  private String buyerPhoneNumber;

  @Column(name = "dealer_name")
  private String dealerName;

  @Column(name = "convenience_store")
  private String convenienceStore;

  @Column(name = "parcel_company")
  private String parcelCompany;

  @Column(name = "invoice_number")
  private String invoiceNumber;

  public void updateStatus(DealStatus status) {
    this.status = status;
  }

  public void updateDepositInfo(
      Account account, UpdateDepositInfoRequest request) {
    if (account != null) {
      this.bank = account.getBank();
      this.accountNumber = account.getAccountNumber();
      this.accountOwnerName = account.getOwnerName();
    }
    if (request.getName() != null && !request.getName().isEmpty()) {
      this.depositorName = request.getName().trim();
    }
    if (request.getDepositedAt() != null && !request.getDepositedAt().isBlank()) {
      this.depositedAt = stringToLocalDateTime(request.getDepositedAt());
    }
    if (request.getDepositPrice() != null && request.getDepositPrice() >= 0) {
      this.depositPrice = request.getDepositPrice();
    }

    if (this.status != DealStatus.DEPOSITED) {
      this.status = DealStatus.DEPOSITED;
    }
  }

  public void updateShippingInfo(UpdateShippingInfoRequest request) {
    if (request.getDealerName() != null && !request.getDealerName().isBlank()) {
      this.dealerName = request.getDealerName().trim();
    }
    if (request.getParcelCompany() != null && !request.getParcelCompany().isBlank()) {
      this.parcelCompany = request.getParcelCompany();
    }
    if (request.getInvoiceNumber() != null && !request.getInvoiceNumber().isBlank()) {
      this.invoiceNumber = request.getInvoiceNumber();
    }

    if (this.status != DealStatus.SHIPPING) {
      this.status = DealStatus.SHIPPING;
    }
  }

  public void updateBuyerInfoParcel(Account account, Address address, String buyerPhoneNumber) {
    if (account != null) {
      this.refundBank = account.getBank();
      this.refundAccountNumber = account.getAccountNumber();
      this.refundAccountOwnerName = account.getOwnerName();
    }

    if (address != null) {
      this.buyerName = address.getName();
      this.buyerZipCode = address.getZipCode();
      this.buyerRoadAddress = address.getRoadAddress();
      this.buyerAddress = address.getCommonAddress();
      this.buyerAddressDetail = address.getDetail();
    }

    if (buyerPhoneNumber != null && !buyerPhoneNumber.isBlank()) {
      this.buyerPhoneNumber = buyerPhoneNumber;
    }
  }

  public void updateBuyerInfoConvenience(Account account, EnrollAndUpdateRequest request) {
    if (account != null) {
      this.refundBank = account.getBank();
      this.refundAccountNumber = account.getAccountNumber();
      this.refundAccountOwnerName = account.getOwnerName();
    }

    if (request.getName() != null && !request.getName().isBlank()) {
      this.buyerName = request.getName().trim();
    }

    if (request.getConvenienceStore() != null && !request.getConvenienceStore().isBlank()) {
      this.convenienceStore = request.getConvenienceStore();
    }

    if (request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank()) {
      this.buyerPhoneNumber = request.getPhoneNumber();
    }
  }

  public void updateBuyerInfoMeeting(EnrollAndUpdateRequest request) {
    if (request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank()) {
      this.buyerPhoneNumber = request.getPhoneNumber();
    }
  }

  @Builder
  public Deal(
      //구매신청 회원 정보
      Long buyerId, Long detailId, DealStatus status,
      //입금계좌정보
      Bank bank, String accountNumber, String accountOwnerName,
      //입금자 정보
      String depositorName, LocalDateTime depositedAt, Long depositPrice,
      //환불계좌정보
      Bank refundBank, String refundAccountNumber, String refundAccountOwnerName,
      //구매자 수령정보
      String buyerName, String buyerPhoneNumber, String convenienceStore,
      String buyerZipCode, String buyerRoadAddress, String buyerAddress, String buyerAddressDetail,
      //발송자(담당자) 발송 정보
      String dealerName, String parcelCompany, String invoiceNumber) {
    this.buyerId = buyerId;
    this.detailId = detailId;
    this.status = status;

    this.bank = bank;
    this.accountNumber = accountNumber;
    this.accountOwnerName = accountOwnerName;
    this.depositorName = depositorName;
    this.depositedAt = depositedAt;
    this.depositPrice = depositPrice;

    this.refundBank = refundBank;
    this.refundAccountNumber = refundAccountNumber;
    this.refundAccountOwnerName = refundAccountOwnerName;

    this.buyerName = buyerName;
    this.buyerPhoneNumber = buyerPhoneNumber;

    this.convenienceStore = convenienceStore;

    this.buyerZipCode = buyerZipCode;
    this.buyerRoadAddress = buyerRoadAddress;
    this.buyerAddress = buyerAddress;
    this.buyerAddressDetail = buyerAddressDetail;

    this.dealerName = dealerName;

    this.parcelCompany = parcelCompany;
    this.invoiceNumber = invoiceNumber;
  }
}
