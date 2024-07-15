package com.zerobase.used_trade.service.impl;

import static com.zerobase.used_trade.util.CountUtility.ADMIN_MAX_ADDRESS;
import static com.zerobase.used_trade.util.CountUtility.USER_MAX_ADDRESS;

import com.zerobase.used_trade.component.PageConverter;
import com.zerobase.used_trade.data.constant.AddressSortType;
import com.zerobase.used_trade.data.constant.UserRole;
import com.zerobase.used_trade.data.domain.Address;
import com.zerobase.used_trade.data.domain.User;
import com.zerobase.used_trade.data.dto.AddressDto.EnrollRequest;
import com.zerobase.used_trade.data.dto.AddressDto.Principle;
import com.zerobase.used_trade.data.dto.AddressDto.UpdateRequest;
import com.zerobase.used_trade.exception.impl.AlreadyExistsAddressException;
import com.zerobase.used_trade.exception.impl.AlreadyMaxCountAddressException;
import com.zerobase.used_trade.exception.impl.CannotDeleteOnlyOneException;
import com.zerobase.used_trade.exception.impl.NoAddressException;
import com.zerobase.used_trade.exception.impl.NoAuthorizeException;
import com.zerobase.used_trade.exception.impl.NoUserException;
import com.zerobase.used_trade.repository.AddressRepository;
import com.zerobase.used_trade.repository.UserRepository;
import com.zerobase.used_trade.service.AddressService;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {
  private final AddressRepository addressRepository;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public Principle enrollAddress(Long userId, EnrollRequest request) {
    //TODO TOKEN 생성 이후엔 CustomUserDetails 로 받을 예정이므로 굳이 불러올 필요 없음
    User user = this.userRepository.findById(userId).orElseThrow(NoUserException::new);

    Long userAddressCount = this.addressRepository.countByUserId(userId);
    if (userAddressCount == null || userAddressCount == 0L) {
      //처음 주소를 등록하려는 경우
      request.setRepresentative(true);
    } else if ((user.getRole() == UserRole.ADMIN && userAddressCount >= ADMIN_MAX_ADDRESS)
    || (user.getRole() != UserRole.ADMIN && userAddressCount >= USER_MAX_ADDRESS)) {
      //이미 등록 가능 개수를 다 채운 경우
      throw new AlreadyMaxCountAddressException();
    } else if (request.isRepresentative()) {
      //기존 계좌가 있는데 지금 등록하려는 계좌를 대표 계좌로 지정하려는 경우
      Address curRepresentaveAddress = this.addressRepository.findByUserIdAndRepresentativeTrue(userId)
          .orElseThrow(NoAddressException::new);

      curRepresentaveAddress.changeRepresentative();
    }

    try {
      Address address = this.addressRepository.save(request.toEntity(userId));

      return Principle.fromEntity(address);
    } catch (DataIntegrityViolationException e) {
      //등록하려는 사용자에게 이미 중복 수신인-주소가 있는 경우
      throw new AlreadyExistsAddressException();
    }
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Principle> getAddressList(Long userId, int page, int size) {
    return new PageConverter<Address>().ListToPage(this.addressRepository.findAllByUserId(userId),
            AddressSortType.REPRESENTATIVETOPCREATEDATASC.comparator(), PageRequest.of(page, size))
        .map(Principle::fromEntity);
  }

  @Override
  @Transactional
  public void updateAddressInfo(Long userId, Long addressId, UpdateRequest request) {
    Address address = this.addressRepository.findById(addressId)
        .orElseThrow(NoAddressException::new);

    if (!Objects.equals(userId, address.getUserId())) {
      throw new NoAuthorizeException();
    }

    //대표주소로 설정하는경우 다른 대표주소 해제
    if (request.isRepresentative() && !address.isRepresentative()) {
      Address curRepresentativeAddress = this.addressRepository.findByUserIdAndRepresentativeTrue(userId)
          .orElseThrow(NoAddressException::new);

      curRepresentativeAddress.changeRepresentative();
    }

    try {
      address.update(request);
    } finally {
      throw new AlreadyExistsAddressException();
    }
  }

  @Override
  @Transactional
  public void deleteAddress(Long userId, Long addressId) {
    Address address = this.addressRepository.findById(addressId)
        .orElseThrow(NoAddressException::new);

    if (!Objects.equals(userId, address.getUserId())) {
      throw new NoAuthorizeException();
    }

    //대표주소인 경우 다른 주소를 대표 주소로 지정하고 삭제
    //다른 주소가 없는 경우 오류
    if (address.isRepresentative()) {
      Address nextRepresentativeAddress =
          this.addressRepository.findFirstByUserIdAndIdNotOrderByIdAsc(userId, addressId)
              .orElseThrow(CannotDeleteOnlyOneException::new);

      nextRepresentativeAddress.changeRepresentative();
    }

    this.addressRepository.delete(address);
  }
}
