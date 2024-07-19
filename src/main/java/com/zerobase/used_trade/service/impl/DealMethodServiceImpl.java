package com.zerobase.used_trade.service.impl;

import static com.zerobase.used_trade.util.DateTimeUtility.stringToLocalDateTime;
import static java.util.Arrays.asList;

import com.zerobase.used_trade.data.constant.DealMethodType;
import com.zerobase.used_trade.data.constant.ProductStatus;
import com.zerobase.used_trade.data.constant.UserRole;
import com.zerobase.used_trade.data.domain.DealMethod;
import com.zerobase.used_trade.data.domain.Product;
import com.zerobase.used_trade.data.domain.User;
import com.zerobase.used_trade.data.dto.DealMethodDto.ConvenienceMethod;
import com.zerobase.used_trade.data.dto.DealMethodDto.EnrollRequest;
import com.zerobase.used_trade.data.dto.DealMethodDto.MeetingMethod;
import com.zerobase.used_trade.data.dto.DealMethodDto.ParcelMethod;
import com.zerobase.used_trade.data.dto.DealMethodDto.ProductDetailMethod;
import com.zerobase.used_trade.exception.impl.CannotDeleteDealMethodException;
import com.zerobase.used_trade.exception.impl.NoAuthorizeException;
import com.zerobase.used_trade.exception.impl.NoDealMethodException;
import com.zerobase.used_trade.exception.impl.NoProductException;
import com.zerobase.used_trade.exception.impl.NoUserException;
import com.zerobase.used_trade.repository.DealMethodRepository;
import com.zerobase.used_trade.repository.DealRepository;
import com.zerobase.used_trade.repository.ProductRepository;
import com.zerobase.used_trade.repository.UserRepository;
import com.zerobase.used_trade.service.DealMethodService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class DealMethodServiceImpl implements DealMethodService {
  private final DealMethodRepository dealMethodRepository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;
  private final DealRepository dealRepository;

  @Override
  public ProductDetailMethod enrollDealMethod(Long userId, EnrollRequest request) {
    User user = this.userRepository.findById(userId).orElseThrow(NoUserException::new);

    //이용 권한 확인
    if (user.getRole() == UserRole.ADMIN
        && !this.productRepository.canUpdatedProductByAdmin(userId, request.getProductId())) {
      throw new NoAuthorizeException();
    } else if (user.getRole() == UserRole.USER
        && !this.productRepository.canUpdatedProductByUser(userId, request.getProductId())) {
      throw new NoAuthorizeException();
    } else if (user.getRole() == UserRole.DENIED) {
      throw new NoAuthorizeException();
    }
    
    //기본 응답
    ProductDetailMethod response = new ProductDetailMethod(request.getProductId());

    if (request.getParcel() != null && request.getParcel().isFlag()) {
      try {
        DealMethod dealMethod = this.dealMethodRepository.save(request.getParcel().toEntity(request.getProductId()));

        response.setParcel(ParcelMethod.fromEntity(dealMethod));
      } catch (DataIntegrityViolationException e) {
        log.error(
            "cannot enroll deal method of product cause it's already exists.-> dealMethodType: {}, productId: {} ",
            DealMethodType.PARCEL.toString(), request.getProductId());
      }
    }

    if (request.getConvenience() != null && request.getConvenience().isFlag()) {
      try {
        DealMethod dealMethod = this.dealMethodRepository.save(
            request.getConvenience().toEntity(request.getProductId()));

        response.setConvenience(ConvenienceMethod.formEntity(dealMethod));
      } catch (DataIntegrityViolationException e) {
        log.error(
            "cannot enroll deal method of product cause it's already exists.-> dealMethodType: {}, productId: {} ",
            DealMethodType.CONVENIENCE.toString(), request.getProductId());
      }
    }

    if (user.getRole() == UserRole.ADMIN) {
      //관리자는 직거래 방식 등록 불가
      log.error(
          "cannot enroll deal method type of meeting cause user's role is admin.-> productId: {} ",
          request.getProductId());
      return response;
    } else if (user.getPhoneNumber() == null || user.getPhoneNumber().isEmpty()) {
      //일반 사용자 중 연락처가 등록 안된사람은 등록 불가
      log.error(
          "cannot enroll deal method type of meeting cause user's phoneNumber is empty.-> productId: {} ",
          request.getProductId());
      return response;
    }

    if (!request.getMeetings().isEmpty()) {
      request.getMeetings().forEach(meeting-> {
        try {
          LocalDateTime dateTime = stringToLocalDateTime(meeting.getDateTime());
          DealMethod dealMethod = this.dealMethodRepository.save(meeting.toEntity(request.getProductId(), dateTime));

          response.getMeeting().add(MeetingMethod.fromEntity(dealMethod));
        } catch (DataIntegrityViolationException e) {
          log.error(
              "cannot enroll deal method of product cause it's already exists.-> dealMethodType: {}, productId: {} ",
              DealMethodType.CONVENIENCE.toString(), request.getProductId());
        }
      });
    }

    return response;
  }

  @Override
  @Transactional(readOnly = true)
  public ProductDetailMethod getDealMethodsByProduct(Long productId) {
    Product product = this.productRepository.findById(productId).orElseThrow(NoProductException::new);

    ProductDetailMethod response = new ProductDetailMethod(productId);
    if (!asList(ProductStatus.SELLING, ProductStatus.PROCESSING).contains(product.getStatus())) {
      return response;
    }
    List<DealMethod> dealMethods = this.dealMethodRepository.findAllByProductIdPossible(productId);

    dealMethods.forEach(method-> {
      if (method.getType() == DealMethodType.PARCEL) {
        response.setParcel(ParcelMethod.fromEntity(method));
      } else if (method.getType() == DealMethodType.CONVENIENCE) {
        response.setConvenience(ConvenienceMethod.formEntity(method));
      } else if (method.getType() == DealMethodType.MEETING) {
        response.getMeeting().add(MeetingMethod.fromEntity(method));
      }
    });

    return response;
  }

  @Override
  @Transactional
  public void deleteDealMethod(Long userId, Long dealMethodId) {
    User user = this.userRepository.findById(userId).orElseThrow(NoUserException::new);
    DealMethod dealMethod = this.dealMethodRepository.findById(dealMethodId).orElseThrow(NoDealMethodException::new);

    //이용 권한 확인
    if (user.getRole() == UserRole.ADMIN
        && !this.productRepository.canUpdatedProductByAdmin(userId, dealMethod.getProductId())) {
      throw new NoAuthorizeException();
    } else if (user.getRole() == UserRole.USER
        && !this.productRepository.canUpdatedProductByUser(userId, dealMethod.getProductId())) {
      throw new NoAuthorizeException();
    } else if (user.getRole() == UserRole.DENIED) {
      throw new NoAuthorizeException();
    }
    
    //진행중이거나 신청중인 거래가 있다면 삭제 불가
    if (this.dealRepository.isApplyingOrProcessingByDealMethodId(dealMethodId)) {
      throw new CannotDeleteDealMethodException();
    }

    this.dealMethodRepository.delete(dealMethod);
  }
}
