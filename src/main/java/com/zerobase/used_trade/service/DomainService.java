package com.zerobase.used_trade.service;

import com.zerobase.used_trade.data.constant.DomainFilterType;
import com.zerobase.used_trade.data.constant.DomainSortType;
import com.zerobase.used_trade.data.constant.EmployeeSortType;
import com.zerobase.used_trade.data.constant.SubscribeType;
import com.zerobase.used_trade.data.dto.DomainDto;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;

public interface DomainService {
  //관리자 도메인 등록
  DomainDto.Principle enrollDomain(DomainDto.EnrollRequest request);

  //관리자 도메인 목록 조회
  Page<DomainDto.Principle> getDomainList(
      @Nullable String searchCompanyName,
      @Nullable String searchDomain, int page, int size,
      DomainSortType criteria, DomainFilterType filter);

  //관리자 도메인 상세정보 확인
  DomainDto.DetailInfoResponse getDomainDetail(Long domainId,
      int page, int size, EmployeeSortType criteria);

  //관리자 도메인 갱신
  void extendPeriodOfDomain(Long domainId, SubscribeType type);

  //관리자 도메인 정보 수정
  void updateDomainInfo(Long domainId, DomainDto.UpdateRequest request);

  //관리자 도메인 삭제
  void deleteDomain(Long domainId);
}
