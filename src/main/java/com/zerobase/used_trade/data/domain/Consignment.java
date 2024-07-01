package com.zerobase.used_trade.data.domain;

import com.zerobase.used_trade.data.constant.Bank;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "consignment")
@Entity
public class Consignment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "consignment_id")
  private Long id;

  @Column(name = "product_id")
  private Long productId;

  @Column(name = "seller_zip_code")
  private String zipCode;

  @Column(name = "seller_road_address")
  private String roadAddress;

  @Column(name = "seller_address")
  private String address;

  @Column(name = "seller_address_detail")
  private String addressDetail;

  @Column(name = "seller_bank")
  @Enumerated(EnumType.STRING)
  private Bank bank;

  @Column(name = "seller_account_number")
  private String accountNumber;

  @Column(name = "parcel_company")
  private String parcelCompany;

  @Column(name = "invoice_number")
  private String invoiceNumber;

  @Column(name = "admin_id")
  private Long adminId;

  @Column(name = "return_parcel_company")
  private String returnParcelCompany;

  @Column(name = "return_invoice_number")
  private String returnInvoiceNumber;

  public void updateAdminId(Long adminId) {
    this.adminId = adminId;
  }

  public void updateParcelInfo(String parcelCompany, String invoiceNumber) {
    this.parcelCompany = parcelCompany;
    this.invoiceNumber = invoiceNumber;
  }

  public void updateReturnInfo(String returnParcelCompany, String returnInvoiceNumber) {
    this.returnParcelCompany = returnParcelCompany;
    this.returnInvoiceNumber = returnInvoiceNumber;
  }

  @Builder
  public Consignment(
      Long productId, String zipCode, String roadAddress, String address, String addressDetail,
      Bank bank, String accountNumber) {
    this.productId = productId;
    this.zipCode = zipCode;
    this.roadAddress = roadAddress;
    this.address = address;
    this.addressDetail = addressDetail;
    this.bank = bank;
    this.accountNumber = accountNumber;
  }
}
