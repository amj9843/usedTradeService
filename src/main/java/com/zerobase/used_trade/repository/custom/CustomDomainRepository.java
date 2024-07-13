package com.zerobase.used_trade.repository.custom;

public interface CustomDomainRepository {
  Long findIdByDomainAddress(String domainAddress);
}
