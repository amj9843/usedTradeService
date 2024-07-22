package com.zerobase.used_trade.service.impl;

import static com.zerobase.used_trade.util.GroupStatusUtility.CANNOT_READ_DEAL_DETAIL_STATUS;
import static com.zerobase.used_trade.util.GroupStatusUtility.CANNOT_UPDATE_BUYER_INFO_DEAL_STATUS;
import static com.zerobase.used_trade.util.GroupStatusUtility.CAN_APPROVE_OR_DENY_DEAL_STATUS;
import static com.zerobase.used_trade.util.GroupStatusUtility.CAN_APPROVE_OR_DENY_PRODUCT_STATUS;
import static com.zerobase.used_trade.util.GroupStatusUtility.CAN_CANCEL_DEAL_STATUS;
import static com.zerobase.used_trade.util.GroupStatusUtility.CAN_COMPLETED_DEAL_STATUS;
import static com.zerobase.used_trade.util.GroupStatusUtility.CAN_COMPLETED_MEETING_DEAL_STATUS;
import static com.zerobase.used_trade.util.GroupStatusUtility.CAN_CONFIRM_DEAL_STATUS;
import static com.zerobase.used_trade.util.GroupStatusUtility.CAN_DELETE_DEAL_STATUS;
import static com.zerobase.used_trade.util.GroupStatusUtility.CAN_REFUND_DEAL_STATUS;
import static com.zerobase.used_trade.util.GroupStatusUtility.CAN_UPDATE_DEPOSIT_INFO_DEAL_STATUS;
import static com.zerobase.used_trade.util.GroupStatusUtility.CAN_UPDATE_SHIPPING_INFO_DEAL_STATUS;
import static java.util.Arrays.asList;

import com.zerobase.used_trade.component.PageConverter;
import com.zerobase.used_trade.data.constant.DealMethodFilterType;
import com.zerobase.used_trade.data.constant.DealMethodType;
import com.zerobase.used_trade.data.constant.DealSortType;
import com.zerobase.used_trade.data.constant.DealStatus;
import com.zerobase.used_trade.data.constant.DealStatusFilterType;
import com.zerobase.used_trade.data.constant.ProductStatus;
import com.zerobase.used_trade.data.domain.Account;
import com.zerobase.used_trade.data.domain.Address;
import com.zerobase.used_trade.data.domain.Deal;
import com.zerobase.used_trade.data.domain.DealMethod;
import com.zerobase.used_trade.data.domain.Product;
import com.zerobase.used_trade.data.domain.User;
import com.zerobase.used_trade.data.dto.AccountDto;
import com.zerobase.used_trade.data.dto.DealDto.DetailDealInfoOfConvenience;
import com.zerobase.used_trade.data.dto.DealDto.DetailDealInfoOfMeeting;
import com.zerobase.used_trade.data.dto.DealDto.DetailDealInfoOfParcel;
import com.zerobase.used_trade.data.dto.DealDto.EnrollAndUpdateRequest;
import com.zerobase.used_trade.data.dto.DealDto.Principle;
import com.zerobase.used_trade.data.dto.DealDto.SimpleInfoResponse;
import com.zerobase.used_trade.data.dto.DealDto.UpdateDepositInfoRequest;
import com.zerobase.used_trade.data.dto.DealDto.UpdateShippingInfoRequest;
import com.zerobase.used_trade.data.dto.DealMethodDto;
import com.zerobase.used_trade.exception.impl.AlreadyExistsDealException;
import com.zerobase.used_trade.exception.impl.CannotApplyDealCauseInsertEmptyException;
import com.zerobase.used_trade.exception.impl.CannotApplyDealException;
import com.zerobase.used_trade.exception.impl.CannotUseBeforeEnrollAccountException;
import com.zerobase.used_trade.exception.impl.CannotUseBeforeEnrollAddressException;
import com.zerobase.used_trade.exception.impl.CannotUseByDealStatusException;
import com.zerobase.used_trade.exception.impl.CannotUseByProductStatusException;
import com.zerobase.used_trade.exception.impl.InvalidAccountOwnerException;
import com.zerobase.used_trade.exception.impl.InvalidRequestException;
import com.zerobase.used_trade.exception.impl.NoAccountException;
import com.zerobase.used_trade.exception.impl.NoAddressException;
import com.zerobase.used_trade.exception.impl.NoAuthorizeException;
import com.zerobase.used_trade.exception.impl.NoDealException;
import com.zerobase.used_trade.exception.impl.NoDealMethodException;
import com.zerobase.used_trade.exception.impl.NoProductException;
import com.zerobase.used_trade.exception.impl.NoUserException;
import com.zerobase.used_trade.repository.AccountRepository;
import com.zerobase.used_trade.repository.AddressRepository;
import com.zerobase.used_trade.repository.DealMethodRepository;
import com.zerobase.used_trade.repository.DealRepository;
import com.zerobase.used_trade.repository.ProductRepository;
import com.zerobase.used_trade.repository.UserRepository;
import com.zerobase.used_trade.service.DealService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class DealServiceImpl implements DealService {
  private final DealMethodRepository dealMethodRepository;
  private final DealRepository dealRepository;
  private final UserRepository userRepository;
  private final AccountRepository accountRepository;
  private final AddressRepository addressRepository;
  private final ProductRepository productRepository;

  @Override
  @Transactional(readOnly = true)
  public DealMethodDto.Principle findDealMethodByDealId(Long dealId) {
    return DealMethodDto.Principle.fromEntity(
        this.dealMethodRepository.findByDealId(dealId).orElseThrow(NoDealMethodException::new));
  }

  @Override
  @Transactional
  public Principle applyDeal(Long userId, Long dealMethodId, EnrollAndUpdateRequest request) {
    DealMethod dealMethod = this.dealMethodRepository.findById(dealMethodId)
        .orElseThrow(NoDealMethodException::new);

    //본인이 등록하거나 판매 담당한 상품이 아니고, 상품의 상태가 거래 가능한 상태인지 확인
    if (!this.productRepository.canApplyDeal(userId, dealMethod.getProductId())) {
      throw new CannotApplyDealException();
    }

    Deal deal;
    switch (dealMethod.getType()) {
      case MEETING -> {
        deal = request.toEntity(dealMethodId, userId, null, null);
      }
      case PARCEL -> {
        Account account = (request.getAccountId() != null && request.getAccountId() > 0) ?
            this.accountRepository.findByIdAndUserId(request.getAccountId(), userId)
                .orElseThrow(NoAccountException::new)
            : this.accountRepository.findByUserIdAndRepresentativeTrue(userId).orElseThrow(
            CannotUseBeforeEnrollAccountException::new);

        Address address = (request.getAddressId() != null && request.getAddressId() > 0) ?
            this.addressRepository.findByIdAndUserId(request.getAddressId(), userId)
                .orElseThrow(NoAddressException::new)
            : this.addressRepository.findByUserIdAndRepresentativeTrue(userId).orElseThrow(
                CannotUseBeforeEnrollAddressException::new);

        deal = request.toEntity(dealMethodId, userId, account, address);
      }
      case CONVENIENCE -> {
        Account account = (request.getAccountId() != null && request.getAccountId() > 0) ?
            this.accountRepository.findByIdAndUserId(request.getAccountId(), userId)
                .orElseThrow(NoAccountException::new)
            : this.accountRepository.findByUserIdAndRepresentativeTrue(userId).orElseThrow(
                CannotUseBeforeEnrollAccountException::new);

        if ((request.getName() == null || request.getName().isBlank())
        || (request.getConvenienceStore() == null || request.getConvenienceStore().isBlank())) {
          throw new CannotApplyDealCauseInsertEmptyException();
        }

        deal = request.toEntity(dealMethodId, userId, account, null);
      }
      default -> throw new RuntimeException();
    }

    try {
      deal = this.dealRepository.save(deal);

      return Principle.fromEntity(deal);
    } catch (DataIntegrityViolationException e) {
      throw new AlreadyExistsDealException();
    }
  }

  @Override
  @Transactional(readOnly = true)
  public Page<SimpleInfoResponse> getDealListBySeller(Long userId, Long productId, int page, int size,
      DealSortType criteria, LocalDateTime applyTimeAfter, LocalDateTime applyTimeBefore,
      DealMethodFilterType methodFilter, DealStatusFilterType statusFilter) {
    //판매 담당자가 아닌 경우 조회 불가
    if (!this.productRepository.isSeller(userId, productId)) {
      throw new NoAuthorizeException();
    }

    List<SimpleInfoResponse> simpleDealList = this.dealRepository.findAllOfSimple(
        productId, null, applyTimeAfter, applyTimeBefore, methodFilter, statusFilter);

    return new PageConverter<SimpleInfoResponse>().ListToPage(simpleDealList,
        criteria.comparator(), PageRequest.of(page, size));
  }

  @Override
  @Transactional(readOnly = true)
  public Page<SimpleInfoResponse> getDealListByBuyer(Long userId, Long productId, int page, int size, DealSortType criteria,
      LocalDateTime applyTimeAfter, LocalDateTime applyTimeBefore, DealMethodFilterType methodFilter,
      DealStatusFilterType statusFilter) {
    List<SimpleInfoResponse> simpleDealList = this.dealRepository.findAllOfSimple(
        productId, userId, applyTimeAfter, applyTimeBefore, methodFilter, statusFilter);

    return new PageConverter<SimpleInfoResponse>().ListToPage(simpleDealList,
        criteria.comparator(), PageRequest.of(page, size));
  }

  @Override
  @Transactional(readOnly = true)
  public DetailDealInfoOfMeeting getDealDetailOfMeeting(Long userId, Long dealId) {
    //읽으려는 거래 내역의 상태에 따라 읽기 불가
    Deal deal = this.dealRepository.findById(dealId).orElseThrow(NoDealException::new);
    if (CANNOT_READ_DEAL_DETAIL_STATUS.contains(deal.getStatus())) {
      throw new CannotUseByDealStatusException();
    }
    
    DetailDealInfoOfMeeting detail = DetailDealInfoOfMeeting.fromEntity(
        deal, findDealMethodByDealId(dealId),
        this.userRepository.findDealerByDealId(dealId).orElseThrow(NoUserException::new)
    );

    if (!Objects.equals(detail.getDealerId(), userId)
        && !Objects.equals(detail.getBuyerId(), userId)) {
      throw new NoAuthorizeException();
    }

    return detail;
  }

  @Override
  @Transactional(readOnly = true)
  public DetailDealInfoOfParcel getDealDetailOfParcel(Long userId, Long dealId) {
    Deal deal = this.dealRepository.findById(dealId).orElseThrow(NoDealException::new);
    if (CANNOT_READ_DEAL_DETAIL_STATUS.contains(deal.getStatus())) {
      throw new CannotUseByDealStatusException();
    }
    
    User dealer = this.userRepository.findDealerByDealId(dealId).orElseThrow(NoUserException::new);
  
    DetailDealInfoOfParcel detail = DetailDealInfoOfParcel.fromEntity(
        deal, findDealMethodByDealId(dealId), dealer,
        this.accountRepository.findAllByUserId(dealer.getId()).stream()
            .map(AccountDto.Principle::fromEntity).toList()
    );

    if (!Objects.equals(detail.getDealerId(), userId)
        && !Objects.equals(detail.getBuyerId(), userId)) {
      throw new NoAuthorizeException();
    }

    return detail;
  }

  @Override
  @Transactional(readOnly = true)
  public DetailDealInfoOfConvenience getDealDetailOfConvenience(Long userId, Long dealId) {
    Deal deal = this.dealRepository.findById(dealId).orElseThrow(NoDealException::new);
    if (CANNOT_READ_DEAL_DETAIL_STATUS.contains(deal.getStatus())) {
      throw new CannotUseByDealStatusException();
    }

    User dealer = this.userRepository.findDealerByDealId(dealId).orElseThrow(NoUserException::new);

    DetailDealInfoOfConvenience detail = DetailDealInfoOfConvenience.fromEntity(
        deal, findDealMethodByDealId(dealId), dealer,
        this.accountRepository.findAllByUserId(dealer.getId()).stream()
            .map(AccountDto.Principle::fromEntity).toList()
    );

    if (!Objects.equals(detail.getDealerId(), userId)
        && !Objects.equals(detail.getBuyerId(), userId)) {
      throw new NoAuthorizeException();
    }

    return detail;
  }

  @Override
  @Transactional
  public void approvedDeal(Long userId, Long dealId) {
    //판매 담당자가 아닌 경우 이용 불가
    if (!this.dealRepository.isSeller(userId, dealId)) {
      throw new NoAuthorizeException();
    }

    Deal deal = this.dealRepository.findById(dealId).orElseThrow(NoDealException::new);
    if (!CAN_APPROVE_OR_DENY_DEAL_STATUS.contains(deal.getStatus())) {
      throw new CannotUseByDealStatusException();
    }

    Product product = this.productRepository.findByDealMethodId(deal.getDetailId())
        .orElseThrow(NoProductException::new);
    if (!CAN_APPROVE_OR_DENY_PRODUCT_STATUS.contains(product.getStatus())) {
      throw new CannotUseByProductStatusException();
    }

    deal.updateStatus(DealStatus.APPROVED);
    product.updateStatus(ProductStatus.PROCESSING);
  }

  @Override
  @Transactional
  public void denyDeal(Long userId, Long dealId) {
    //판매 담당자가 아닌 경우 이용 불가
    if (!this.dealRepository.isSeller(userId, dealId)) {
      throw new NoAuthorizeException();
    }

    Deal deal = this.dealRepository.findById(dealId).orElseThrow(NoDealException::new);
    if (!CAN_APPROVE_OR_DENY_DEAL_STATUS.contains(deal.getStatus())) {
      throw new CannotUseByDealStatusException();
    }

    Product product = this.productRepository.findByDealMethodId(deal.getDetailId())
        .orElseThrow(NoProductException::new);
    if (!CAN_APPROVE_OR_DENY_PRODUCT_STATUS.contains(product.getStatus())) {
      throw new CannotUseByProductStatusException();
    }

    deal.updateStatus(DealStatus.DENIED);
  }

  @Override
  @Transactional
  public void cancelDeal(Long userId, Long dealId) {
    //판매 담당자/구매자가 아닌 경우 이용 불가
    if (!this.dealRepository.isSeller(userId, dealId) && !this.dealRepository.existsByIdAndBuyerId(dealId, userId)) {
      throw new NoAuthorizeException();
    }

    Deal deal = this.dealRepository.findById(dealId).orElseThrow(NoDealException::new);
    if (!CAN_CANCEL_DEAL_STATUS.contains(deal.getStatus())) {
      throw new CannotUseByDealStatusException();
    }

    Product product = this.productRepository.findByDealMethodId(deal.getDetailId())
        .orElseThrow(NoProductException::new);

    deal.updateStatus(DealStatus.CANCELED);
    if (product.getStatus() == ProductStatus.PROCESSING) {
      product.updateStatus(ProductStatus.SELLING);
    }
  }

  @Override
  @Transactional
  public void updateDepositInfo(Long userId, Long dealId, UpdateDepositInfoRequest request) {
    //구매자가 아닌 경우 이용 불가
    if (!this.dealRepository.existsByIdAndBuyerId(dealId, userId)) {
      throw new NoAuthorizeException();
    }

    Deal deal = this.dealRepository.findById(dealId).orElseThrow(NoDealException::new);
    if (!CAN_UPDATE_DEPOSIT_INFO_DEAL_STATUS.contains(deal.getStatus())) {
      throw new CannotUseByDealStatusException();
    }
    
    Account account = this.accountRepository.findById(request.getAccountId()).orElse(null);
    //처음 등록할 땐 모든 정보를 다 입력해야 함
    if (deal.getAccountNumber() == null && asList(
        account == null,
        request.getName() == null || request.getName().isBlank(),
        request.getDepositedAt() == null || request.getDepositedAt().isBlank(),
        request.getDepositPrice() == null || request.getDepositPrice() < 0).contains(true)) {
      throw new InvalidRequestException();
    }
    
    //계좌 소유주가 판매 담당자가 아닌 경우
    if (account != null && !this.dealRepository.isSeller(account.getUserId(), dealId)) {
      throw new InvalidAccountOwnerException();
    }
    
    deal.updateDepositInfo(account, request);
  }

  @Override
  @Transactional
  public void confirmDeposit(Long userId, Long dealId) {
    //판매 담당자가 아닌 경우 이용 불가
    if (!this.dealRepository.isSeller(userId, dealId)) {
      throw new NoAuthorizeException();
    }

    Deal deal = this.dealRepository.findById(dealId).orElseThrow(NoDealException::new);

    if (!CAN_CONFIRM_DEAL_STATUS.contains(deal.getStatus())) {
      throw new CannotUseByDealStatusException();
    }

    deal.updateStatus(DealStatus.CONFIRMED);
  }

  @Override
  @Transactional
  public void refundDeal(Long userId, Long dealId) {
    //판매 담당자가 아닌 경우 이용 불가
    if (!this.dealRepository.isSeller(userId, dealId)) {
      throw new NoAuthorizeException();
    }

    Deal deal = this.dealRepository.findById(dealId).orElseThrow(NoDealException::new);
    if (!CAN_REFUND_DEAL_STATUS.contains(deal.getStatus())) {
      throw new CannotUseByDealStatusException();
    }

    Product product = this.productRepository.findByDealMethodId(deal.getDetailId())
        .orElseThrow(NoProductException::new);

    deal.updateStatus(DealStatus.REFUNDED);
    product.updateStatus(ProductStatus.SELLING);
  }

  @Override
  @Transactional
  public void updateShippingInfo(Long userId, Long dealId, UpdateShippingInfoRequest request) {
    //판매 담당자가 아닌 경우 이용 불가
    if (!this.dealRepository.isSeller(userId, dealId)) {
      throw new NoAuthorizeException();
    }

    Deal deal = this.dealRepository.findById(dealId).orElseThrow(NoDealException::new);
    if (!CAN_UPDATE_SHIPPING_INFO_DEAL_STATUS.contains(deal.getStatus())) {
      throw new CannotUseByDealStatusException();
    }

    DealMethodDto.Principle dealMethod = findDealMethodByDealId(dealId);

    //처음 등록할 땐 모든 정보를 다 입력해야 함
    if (deal.getDealerName() == null && asList(
        request.getDealerName() == null || request.getDealerName().isBlank(),
        request.getInvoiceNumber() == null || request.getInvoiceNumber().isBlank(),
        dealMethod.getType() == DealMethodType.PARCEL
            && (request.getParcelCompany() == null || request.getParcelCompany().isBlank())).contains(true)) {
      throw new InvalidRequestException();
    }

    if (dealMethod.getType() == DealMethodType.CONVENIENCE
        && (request.getParcelCompany() != null && !request.getParcelCompany().isBlank())) {
      request.setParcelCompany(null);
    }

    deal.updateShippingInfo(request);
  }

  @Override
  @Transactional
  public void completeDeal(Long userId, Long dealId) {
    //구매자가 아닌 경우 이용 불가
    if (!this.dealRepository.existsByIdAndBuyerId(dealId, userId)) {
      throw new NoAuthorizeException();
    }

    Deal deal = this.dealRepository.findById(dealId).orElseThrow(NoDealException::new);
    DealMethodDto.Principle dealMethod = findDealMethodByDealId(dealId);

    if ((dealMethod.getType() == DealMethodType.MEETING
    && !CAN_COMPLETED_MEETING_DEAL_STATUS.contains(deal.getStatus()))
    || !CAN_COMPLETED_DEAL_STATUS.contains(deal.getStatus())) {
      throw new CannotUseByDealStatusException();
    }

    Product product = this.productRepository.findByDealMethodId(deal.getDetailId())
        .orElseThrow(NoProductException::new);

    deal.updateStatus(DealStatus.COMPLETED);
    product.updateStatus(ProductStatus.SELLED);

    this.dealRepository.findAllByProductIdExceptDealId(product.getId(), dealId)
        .forEach(anotherDeal-> {
          if (anotherDeal.getStatus() == DealStatus.APPLIED) {
            anotherDeal.updateStatus(DealStatus.CANCELED);
          }
        });
  }

  @Override
  @Transactional
  public void updateDealInfo(Long userId, Long dealId, EnrollAndUpdateRequest request) {
    //구매자가 아닌 경우 이용 불가
    if (!this.dealRepository.existsByIdAndBuyerId(dealId, userId)) {
      throw new NoAuthorizeException();
    }

    Deal deal = this.dealRepository.findById(dealId).orElseThrow(NoDealException::new);
    if (CANNOT_UPDATE_BUYER_INFO_DEAL_STATUS.contains(deal.getStatus())) {
      throw new CannotUseByDealStatusException();
    }

    DealMethodDto.Principle dealMethod = findDealMethodByDealId(dealId);
    switch (dealMethod.getType()) {
      case MEETING -> {
        deal.updateBuyerInfoMeeting(request);
      }
      case PARCEL -> {
        Account account = this.accountRepository.findByIdAndUserId(request.getAccountId(), userId)
                .orElse(null);

        Address address = this.addressRepository.findByIdAndUserId(request.getAddressId(), userId)
                .orElse(null);

        deal.updateBuyerInfoParcel(account, address, request.getPhoneNumber());
      }
      case CONVENIENCE -> {
        Account account = this.accountRepository.findByIdAndUserId(request.getAccountId(), userId)
            .orElse(null);

        deal.updateBuyerInfoConvenience(account, request);
      }
      default -> throw new RuntimeException();
    }
  }

  @Override
  @Transactional
  public void deleteDeal(Long userId, Long dealId) {
    //구매자가 아닌 경우 이용 불가
    if (!this.dealRepository.existsByIdAndBuyerId(dealId, userId)) {
      throw new NoAuthorizeException();
    }

    Deal deal = this.dealRepository.findById(dealId).orElseThrow(NoDealException::new);
    if (!CAN_DELETE_DEAL_STATUS.contains(deal.getStatus())) {
      throw new CannotUseByDealStatusException();
    }

    this.dealRepository.delete(deal);
  }
}
