package com.zerobase.used_trade.data.dto;

import com.zerobase.used_trade.annotation.EmptyOrNotBlank;
import com.zerobase.used_trade.annotation.Number;
import com.zerobase.used_trade.annotation.ShortString;
import com.zerobase.used_trade.annotation.ValidEnum;
import com.zerobase.used_trade.data.constant.Bank;
import com.zerobase.used_trade.data.domain.Account;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AccountDto {
  @Data
  @Builder
  public static class Principle {
    private Long id;
    private Long userId;
    private Bank bank;
    private String accountNumber;
    private String ownerName;
    private boolean representative;

    public static Principle fromEntity(Account account) {
      return Principle.builder()
          .id(account.getId())
          .userId(account.getUserId())
          .bank(account.getBank())
          .accountNumber(account.getAccountNumber())
          .ownerName(account.getOwnerName())
          .representative(account.isRepresentative())
          .build();
    }
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class EnrollRequest {
    @NotEmpty(message = "{validation.Account.bank.NotEmpty}")
    @ValidEnum(enumClass = Bank.class)
    private String bank;

    @NotEmpty(message = "{validation.Account.accountNumber.NotEmpty}")
    @Number
    private String accountNumber;

    @NotBlank(message = "{validation.Account.ownerName.NotBlank}")
    @ShortString
    private String ownerName;

    private boolean representative= false;

    public Account toEntity(Long userId) {
      return Account.builder()
          .userId(userId)
          .bank(Bank.valueOf(this.bank))
          .accountNumber(this.accountNumber)
          .ownerName(this.ownerName.trim())
          .representative(this.representative)
          .build();
    }
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UpdateRequest{
    @EmptyOrNotBlank
    @ValidEnum(enumClass = Bank.class)
    private String bank;

    @EmptyOrNotBlank
    @Number
    private String AccountNumber;

    @EmptyOrNotBlank
    @ShortString
    private String ownerName;

    private boolean representative= false;
  }
}
