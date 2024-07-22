package com.zerobase.used_trade.service;

import com.zerobase.used_trade.data.constant.DealMethodFilterType;
import com.zerobase.used_trade.data.constant.DealSortType;
import com.zerobase.used_trade.data.constant.DealStatusFilterType;
import com.zerobase.used_trade.data.dto.DealDto.DetailDealInfoOfConvenience;
import com.zerobase.used_trade.data.dto.DealDto.DetailDealInfoOfMeeting;
import com.zerobase.used_trade.data.dto.DealDto.DetailDealInfoOfParcel;
import com.zerobase.used_trade.data.dto.DealDto.EnrollAndUpdateRequest;
import com.zerobase.used_trade.data.dto.DealDto.Principle;
import com.zerobase.used_trade.data.dto.DealDto.SimpleInfoResponse;
import com.zerobase.used_trade.data.dto.DealDto.UpdateDepositInfoRequest;
import com.zerobase.used_trade.data.dto.DealDto.UpdateShippingInfoRequest;
import com.zerobase.used_trade.data.dto.DealMethodDto;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;

public interface DealService {
  DealMethodDto.Principle findDealMethodByDealId(Long dealId);

  Principle applyDeal(Long userId, Long dealMethodId, EnrollAndUpdateRequest request);

  Page<SimpleInfoResponse> getDealListBySeller(Long userId, Long productId, int page, int size, DealSortType criteria,
      LocalDateTime applyTimeAfter, LocalDateTime applyTimeBefore,
      DealMethodFilterType methodFilter, DealStatusFilterType statusFilter);

  Page<SimpleInfoResponse> getDealListByBuyer(Long userId, Long productId, int page, int size, DealSortType criteria,
      LocalDateTime applyTimeAfter, LocalDateTime applyTimeBefore,
      DealMethodFilterType methodFilter, DealStatusFilterType statusFilter);

  DetailDealInfoOfMeeting getDealDetailOfMeeting(Long userId, Long dealId);
  DetailDealInfoOfParcel getDealDetailOfParcel(Long userId, Long dealId);
  DetailDealInfoOfConvenience getDealDetailOfConvenience(Long userId, Long dealId);

  void approvedDeal(Long userId, Long dealId);
  void denyDeal(Long userId, Long dealId);
  void cancelDeal(Long userId, Long dealId);
  void updateDepositInfo(Long userId, Long dealId, UpdateDepositInfoRequest request);
  void confirmDeposit(Long userId, Long dealId);
  void refundDeal(Long userId, Long dealId);
  void updateShippingInfo(Long userId, Long dealId, UpdateShippingInfoRequest request);
  void completeDeal(Long userId, Long dealId);
  void updateDealInfo(Long userId, Long dealId, EnrollAndUpdateRequest request);
  void deleteDeal(Long userId, Long dealId);
}
