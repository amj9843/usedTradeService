package com.zerobase.used_trade.data.domain;

import com.zerobase.used_trade.data.constant.Bank;
import com.zerobase.used_trade.data.constant.DealStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "deal_method")
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

  @Column(name = "depositor_name")
  private String depositorName;

  @Column(name = "deposited_at")
  private LocalDateTime deposited_at;

  @Column(name = "deposit_price")
  private Long depositPrice;

  @Column(name = "refund_account_bank")
  @Enumerated(EnumType.STRING)
  private Bank refundBank;

  @Column(name = "refund_account_number")
  private String refundAccountNumber;

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
      Bank bank, String accountNumber,
      String depositorName, LocalDateTime deposited_at, Long depositPrice) {
    this.bank = bank;
    this.accountNumber = accountNumber;
    this.depositorName = depositorName;
    this.deposited_at = deposited_at;
    this.depositPrice = depositPrice;
    this.status = DealStatus.DEPOSITED;
  }

  public void updateParcelInfo(String dealerName, String parcelCompany, String invoiceNumber) {
    this.dealerName = dealerName;
    this.parcelCompany = parcelCompany;
    this.invoiceNumber = invoiceNumber;
    this.status = DealStatus.SHIPPING;
  }

  public void updateInvoiceInfo(String dealerName, String invoiceNumber) {
    this.dealerName = dealerName;
    this.invoiceNumber = invoiceNumber;
    this.status = DealStatus.SHIPPING;
  }

  @Builder
  public Deal(
      Long buyerId, Long detailId, DealStatus status,
      Bank refundBank, String refundAccountNumber,
      String buyerName, String buyerPhoneNumber, String convenienceStore,
      String buyerZipCode, String buyerRoadAddress, String buyerAddress, String buyerAddressDetail) {
    this.buyerId = buyerId;
    this.detailId = detailId;
    this.status = status;
    this.refundBank = refundBank;
    this.refundAccountNumber = refundAccountNumber;
    this.buyerName = buyerName;
    this.buyerPhoneNumber = buyerPhoneNumber;
    this.convenienceStore = convenienceStore;
    this.buyerZipCode = buyerZipCode;
    this.buyerRoadAddress = buyerRoadAddress;
    this.buyerAddress = buyerAddress;
    this.buyerAddressDetail = buyerAddressDetail;
  }
}
