package com.zerobase.used_trade.controller;

import static com.zerobase.used_trade.util.DateTimeUtility.stringToLocalDateTime;

import com.zerobase.used_trade.annotation.PassedTime;
import com.zerobase.used_trade.annotation.TimeString;
import com.zerobase.used_trade.annotation.ValidEnum;
import com.zerobase.used_trade.data.constant.DealMethodFilterType;
import com.zerobase.used_trade.data.constant.DealSortType;
import com.zerobase.used_trade.data.constant.DealStatusFilterType;
import com.zerobase.used_trade.data.constant.SuccessCode;
import com.zerobase.used_trade.data.constant.UserRole;
import com.zerobase.used_trade.data.dto.DealDto.EnrollAndUpdateRequest;
import com.zerobase.used_trade.data.dto.DealDto.UpdateDepositInfoRequest;
import com.zerobase.used_trade.data.dto.DealDto.UpdateShippingInfoRequest;
import com.zerobase.used_trade.data.dto.DealMethodDto;
import com.zerobase.used_trade.data.dto.ResultDto;
import com.zerobase.used_trade.data.dto.UserDto.Principle;
import com.zerobase.used_trade.exception.impl.CannotApplyDealBeforeEnrollPhoneNumberException;
import com.zerobase.used_trade.exception.impl.NoAuthorizeException;
import com.zerobase.used_trade.service.DealService;
import com.zerobase.used_trade.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/deal")
public class DealController {
  private final DealService dealService;
  private final UserService userService;

  @Operation(summary = "거래 방식 식별번호로 거래 신청")
  @PostMapping("/{dealMethodId}")
  public ResponseEntity<?> applyDeal(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @PathVariable("dealMethodId") Long dealMethodId,
      @Validated @RequestBody EnrollAndUpdateRequest request) {
    Principle user = this.userService.findUserById(userId);
    if (user.getRole() != UserRole.USER) {
      //일반 사용자 외 사용 불가
      throw new NoAuthorizeException();
    }
    
    //핸드폰번호 입력/등록 모두 하지 않은 사람 이용 불가
    if ((user.getPhoneNumber() == null || user.getPhoneNumber().isEmpty())
        && (request.getPhoneNumber() == null || request.getPhoneNumber().isEmpty())) {
      throw new CannotApplyDealBeforeEnrollPhoneNumberException();
    } else if (request.getPhoneNumber() == null || request.getPhoneNumber().isEmpty()) {
      request.setPhoneNumber(user.getPhoneNumber());
    }

    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.CREATED_SUCCESS.status(), SuccessCode.CREATED_SUCCESS.message(),
            this.dealService.applyDeal(userId, dealMethodId, request))
    );
  }

  @Operation(summary = "판매 담당자의 상품별 거래내역 목록 조회")
  @GetMapping("/seller/{productId}")
  public ResponseEntity<?> getDealListBySeller(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @PathVariable("productId") Long productId,
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size,
      @RequestParam(name = "criteria", required = false, defaultValue = "CREATEDATASC")
      @ValidEnum(enumClass = DealSortType.class) String criteria,
      @RequestParam(name = "applyTimeAfter", required = false) @PassedTime String  applyTimeAfter,
      @RequestParam(name = "applyTimeBefore", required = false) @TimeString String applyTimeBefore,
      @RequestParam(name = "dealMethodFilter", required = false, defaultValue = "ALL")
      @ValidEnum(enumClass = DealMethodFilterType.class) String methodFilter,
      @RequestParam(name = "dealStatusFilter", required = false, defaultValue = "ALL")
      @ValidEnum(enumClass = DealStatusFilterType.class) String statusFilter
  ) {
    return ResponseEntity.ok(
        ResultDto.res(
            SuccessCode.READ_SUCCESS.status(), SuccessCode.READ_SUCCESS.message(),
            this.dealService.getDealListBySeller(userId, productId, page, size, DealSortType.valueOf(criteria),
                stringToLocalDateTime(applyTimeAfter), stringToLocalDateTime(applyTimeBefore),
                DealMethodFilterType.valueOf(methodFilter), DealStatusFilterType.valueOf(statusFilter)))
    );
  }

  @Operation(summary = "구매자의 상품별 거래내역 목록 조회")
  @GetMapping("/buyer/{productId}")
  public ResponseEntity<?> getDealListByBuyer(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @PathVariable("productId") Long productId,
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size,
      @RequestParam(name = "criteria", required = false, defaultValue = "CREATEDATASC")
      @ValidEnum(enumClass = DealSortType.class) String criteria,
      @RequestParam(name = "applyTimeAfter", required = false) @PassedTime String  applyTimeAfter,
      @RequestParam(name = "applyTimeBefore", required = false) @TimeString String applyTimeBefore,
      @RequestParam(name = "dealMethodFilter", required = false, defaultValue = "ALL")
      @ValidEnum(enumClass = DealMethodFilterType.class) String methodFilter,
      @RequestParam(name = "dealStatusFilter", required = false, defaultValue = "ALL")
      @ValidEnum(enumClass = DealStatusFilterType.class) String statusFilter
  ) {
    return ResponseEntity.ok(
        ResultDto.res(
            SuccessCode.READ_SUCCESS.status(), SuccessCode.READ_SUCCESS.message(),
            this.dealService.getDealListByBuyer(userId, productId, page, size, DealSortType.valueOf(criteria),
                stringToLocalDateTime(applyTimeAfter), stringToLocalDateTime(applyTimeBefore),
                DealMethodFilterType.valueOf(methodFilter), DealStatusFilterType.valueOf(statusFilter)))
    );
  }

  @Operation(summary = "거래내역 상세 조회")
  @GetMapping("/detail/{dealId}")
  public ResponseEntity<?> getDealDetail(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @PathVariable("dealId") Long dealId
  ) {
    DealMethodDto.Principle dealMethod = this.dealService.findDealMethodByDealId(dealId);

    ResultDto<?, ?> response = null;
    switch (dealMethod.getType()) {
      case PARCEL -> response = ResultDto.res(
          SuccessCode.READ_SUCCESS.status(), SuccessCode.READ_SUCCESS.message(),
          this.dealService.getDealDetailOfParcel(userId, dealId));
      case MEETING -> response = ResultDto.res(
          SuccessCode.READ_SUCCESS.status(), SuccessCode.READ_SUCCESS.message(),
          this.dealService.getDealDetailOfMeeting(userId, dealId));
      case CONVENIENCE -> response = ResultDto.res(
          SuccessCode.READ_SUCCESS.status(), SuccessCode.READ_SUCCESS.message(),
          this.dealService.getDealDetailOfConvenience(userId, dealId));
      default -> throw new RuntimeException();
    }

    return ResponseEntity.ok(response);
  }

  @Operation(summary = "판매 담당자의 거래 승인")
  @PatchMapping("/approve/{dealId}")
  public ResponseEntity<?> approvedDeal(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @PathVariable("dealId") Long dealId
  ) {
    this.dealService.approvedDeal(userId, dealId);

    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.UPDATED_SUCCESS.status(), SuccessCode.UPDATED_SUCCESS.message())
    );
  }

  @Operation(summary = "판매 담당자의 거래 거절")
  @PatchMapping("/deny/{dealId}")
  public ResponseEntity<?> denyDeal(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @PathVariable("dealId") Long dealId
  ) {
    this.dealService.denyDeal(userId, dealId);

    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.UPDATED_SUCCESS.status(), SuccessCode.UPDATED_SUCCESS.message())
    );
  }

  @Operation(summary = "판매 담당자의 거래 취소")
  @PatchMapping("/cancel/{dealId}")
  public ResponseEntity<?> cancelDeal(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @PathVariable("dealId") Long dealId
  ) {
    this.dealService.cancelDeal(userId, dealId);

    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.UPDATED_SUCCESS.status(), SuccessCode.UPDATED_SUCCESS.message())
    );
  }

  @Operation(summary = "구매자의 입금 정보 입력/수정")
  @PatchMapping("/deposit/{dealId}")
  public ResponseEntity<?> updateDepositInfo(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @PathVariable("dealId") Long dealId,
      @Validated @RequestBody UpdateDepositInfoRequest request
  ) {
    this.dealService.updateDepositInfo(userId, dealId, request);

    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.UPDATED_SUCCESS.status(), SuccessCode.UPDATED_SUCCESS.message())
    );
  }

  @Operation(summary = "판매 담당자의 입금 확인")
  @PatchMapping("/confirm-deposit/{dealId}")
  public ResponseEntity<?> confirmDeposit(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @PathVariable("dealId") Long dealId
  ) {
    this.dealService.confirmDeposit(userId, dealId);

    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.UPDATED_SUCCESS.status(), SuccessCode.UPDATED_SUCCESS.message())
    );
  }

  @Operation(summary = "판매 담당자의 거래 환불")
  @PatchMapping("/refund/{dealId}")
  public ResponseEntity<?> refundDeal(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @PathVariable("dealId") Long dealId
  ) {
    this.dealService.refundDeal(userId, dealId);

    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.UPDATED_SUCCESS.status(), SuccessCode.UPDATED_SUCCESS.message())
    );
  }

  @Operation(summary = "판매 담당자의 배송 정보 입력/수정")
  @PatchMapping("/shipping-info/{dealId}")
  public ResponseEntity<?> updateShippingInfo(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @PathVariable("dealId") Long dealId,
      @Validated @RequestBody UpdateShippingInfoRequest request
  ) {
    this.dealService.updateShippingInfo(userId, dealId, request);

    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.UPDATED_SUCCESS.status(), SuccessCode.UPDATED_SUCCESS.message())
    );
  }

  @Operation(summary = "구매자의 거래 완료")
  @PatchMapping("/complete/{dealId}")
  public ResponseEntity<?> completeDeal(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @PathVariable("dealId") Long dealId
  ) {
    this.dealService.completeDeal(userId, dealId);

    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.UPDATED_SUCCESS.status(), SuccessCode.UPDATED_SUCCESS.message())
    );
  }

  @Operation(summary = "구매자의 거래 정보 수정")
  @PatchMapping("/buyerInfo/{dealId}")
  public ResponseEntity<?> updateDealInfo(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @PathVariable("dealId") Long dealId,
      @Validated @RequestBody EnrollAndUpdateRequest request
  ) {
    this.dealService.updateDealInfo(userId, dealId, request);

    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.UPDATED_SUCCESS.status(), SuccessCode.UPDATED_SUCCESS.message())
    );
  }

  @Operation(summary = "구매자의 거래 삭제")
  @DeleteMapping("/{dealId}")
  public ResponseEntity<?> deleteDeal(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @PathVariable("dealId") Long dealId
  ) {
    this.dealService.deleteDeal(userId, dealId);

    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.DELETED_SUCCESS.status(), SuccessCode.DELETED_SUCCESS.message())
    );
  }
}
