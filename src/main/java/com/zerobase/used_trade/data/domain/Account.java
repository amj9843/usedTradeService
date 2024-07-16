package com.zerobase.used_trade.data.domain;

import com.zerobase.used_trade.data.constant.Bank;
import com.zerobase.used_trade.data.dto.AccountDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "account", uniqueConstraints = {
    @UniqueConstraint(
        columnNames = {"user_id", "bank", "account_number", "owner_name"}
    )
})
@Entity
public class Account extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "account_id")
  private Long id;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "bank")
  @Enumerated(EnumType.STRING)
  private Bank bank;

  @Column(name = "account_number")
  private String accountNumber;

  @Column(name = "owner_name")
  private String ownerName;

  @Column(name = "representative")
  private boolean representative;

  public void update(AccountDto.UpdateRequest request) {
    if (request.getBank() != null && !request.getBank().isBlank()) {
      this.bank = Bank.valueOf(request.getBank());
    }
    if (request.getAccountNumber() != null && !request.getAccountNumber().isBlank()) {
      this.accountNumber = request.getAccountNumber();
    }
    if (request.getOwnerName() != null && !request.getOwnerName().isBlank()) {
      this.ownerName = request.getOwnerName().trim();
    }
    if (request.isRepresentative()) {
      this.representative = true;
    }
  }

  public void changeRepresentative() {
    this.representative = !this.representative;
  }

  @Builder
  public Account(Long userId, Bank bank, String accountNumber, String ownerName, boolean representative) {
    this.userId = userId;
    this.bank = bank;
    this.accountNumber = accountNumber;
    this.ownerName = ownerName;
    this.representative = representative;
  }
}
