package com.zerobase.used_trade.service;

import com.zerobase.used_trade.data.dto.AccountDto.EnrollRequest;
import com.zerobase.used_trade.data.dto.AccountDto.Principle;
import com.zerobase.used_trade.data.dto.AccountDto.UpdateRequest;
import org.springframework.data.domain.Page;

public interface AccountService {
  Principle enrollAccount(Long userId, EnrollRequest request);

  Page<Principle> getAccountList(Long userId, int page, int size);

  void updateAccountInfo(Long userId, Long accountId, UpdateRequest request);

  void deleteAccount(Long userId, Long accountId);
}
