package com.zerobase.used_trade.service;

import com.zerobase.used_trade.data.dto.AddressDto.EnrollRequest;
import com.zerobase.used_trade.data.dto.AddressDto.Principle;
import com.zerobase.used_trade.data.dto.AddressDto.UpdateRequest;
import org.springframework.data.domain.Page;

public interface AddressService {
  Principle enrollAddress(Long userId, EnrollRequest request);

  Page<Principle> getAddressList(Long userId, int page, int size);

  void updateAddressInfo(Long userId, Long addressId, UpdateRequest request);

  void deleteAddress(Long userId, Long addressId);
}
