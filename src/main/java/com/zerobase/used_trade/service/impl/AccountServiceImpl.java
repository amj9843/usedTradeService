package com.zerobase.used_trade.service.impl;

import static com.zerobase.used_trade.util.CountUtility.USER_MAX_ACCOUNT;

import com.zerobase.used_trade.component.PageConverter;
import com.zerobase.used_trade.data.constant.AccountSortType;
import com.zerobase.used_trade.data.constant.Bank;
import com.zerobase.used_trade.data.constant.UserRole;
import com.zerobase.used_trade.data.domain.Account;
import com.zerobase.used_trade.data.domain.User;
import com.zerobase.used_trade.data.dto.AccountDto.EnrollRequest;
import com.zerobase.used_trade.data.dto.AccountDto.Principle;
import com.zerobase.used_trade.data.dto.AccountDto.UpdateRequest;
import com.zerobase.used_trade.exception.impl.AlreadyExistsAccountException;
import com.zerobase.used_trade.exception.impl.AlreadyMaxCountAccountException;
import com.zerobase.used_trade.exception.impl.CannotDeleteOnlyOneException;
import com.zerobase.used_trade.exception.impl.InvalidAccountNumberException;
import com.zerobase.used_trade.exception.impl.NoAccountException;
import com.zerobase.used_trade.exception.impl.NoAuthorizeException;
import com.zerobase.used_trade.exception.impl.NoUserException;
import com.zerobase.used_trade.repository.AccountRepository;
import com.zerobase.used_trade.repository.UserRepository;
import com.zerobase.used_trade.service.AccountService;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
  private final AccountRepository accountRepository;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public Principle enrollAccount(Long userId, EnrollRequest request) {
    //TODO TOKEN 생성 이후엔 CustomUserDetails 로 받을 예정이므로 굳이 불러올 필요 없음
    User user = this.userRepository.findById(userId).orElseThrow(NoUserException::new);
    if (user.getRole() == UserRole.ADMIN) {
      throw new NoAuthorizeException();
    }

    //등록하려는 계좌번호가 은행의 계좌번호 숫자 수와 맞는지 확인
    Bank bank = Bank.valueOf(request.getBank());
    if (!bank.isMatch(request.getAccountNumber())) {
      throw new InvalidAccountNumberException();
    }

    //대표계좌 설정 여부
    Long userAccountCount = this.accountRepository.countByUserId(userId);
    if (userAccountCount == null || userAccountCount == 0L) {
      //기존 계좌에 등록된 계좌가 없는 경우: 반드시 대표 계좌로 지정
      request.setRepresentative(true);
    } else if (userAccountCount >= USER_MAX_ACCOUNT) {
      throw new AlreadyMaxCountAccountException();
    } else if (request.isRepresentative()) {
      //기존 계좌에 등록된 계좌가 있고, 이번에 등록하려는 계좌를 대표 계좌로 등록하려는 경우
      //기존 대표 계좌를 대표 해제시킨 뒤 등록
      Account curRepresentaveAccount = this.accountRepository.findByUserIdAndRepresentativeTrue(userId)
          .orElseThrow(NoAccountException::new);

      curRepresentaveAccount.changeRepresentative();
    }

    try {
      Account account = this.accountRepository.save(request.toEntity(userId));

      return Principle.fromEntity(account);
    } catch (DataIntegrityViolationException e) {
      //이용자의 계좌 정보 목록에 이미 동일한 정보가 있어 저장되지 않은 경우
      throw new AlreadyExistsAccountException();
    }
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Principle> getAccountList(Long userId, int page, int size) {
    //TODO TOKEN 생성 이후엔 CustomUserDetails 로 받을 예정이므로 굳이 불러올 필요 없음
    User user = this.userRepository.findById(userId).orElseThrow(NoUserException::new);
    if (user.getRole() == UserRole.ADMIN) {
      throw new NoAuthorizeException();
    }

    return new PageConverter<Account>().ListToPage(this.accountRepository.findAllByUserId(userId),
        AccountSortType.REPRESENTATIVETOPCREATEDATASC.comparator(), PageRequest.of(page, size))
        .map(Principle::fromEntity);
  }

  @Override
  @Transactional
  public void updateAccountInfo(Long userId, Long accountId, UpdateRequest request) {
    //TODO TOKEN 생성 이후엔 CustomUserDetails 로 받을 예정이므로 굳이 불러올 필요 없음
    User user = this.userRepository.findById(userId).orElseThrow(NoUserException::new);
    if (user.getRole() == UserRole.ADMIN) {
      throw new NoAuthorizeException();
    }

    Account account = this.accountRepository.findById(accountId)
        .orElseThrow(NoAccountException::new);

    if (!Objects.equals(userId, account.getUserId())) {
      throw new NoAuthorizeException();
    }

    //대표계좌로 설정하는경우 기존 대표계좌 해제
    if (request.isRepresentative() && !account.isRepresentative()) {
      Account curRepresentativeAccount = this.accountRepository.findByUserIdAndRepresentativeTrue(userId)
          .orElseThrow(NoAccountException::new);

      curRepresentativeAccount.changeRepresentative();
    }

    try {
      //계좌번호를 변경하는 경우 은행에 속하는 계좌번호가 맞는지 확인
      if (request.getAccountNumber() == null || request.getAccountNumber().isBlank()) {
        account.update(request);
        return;
      }

      Bank bank = (request.getBank() != null && !request.getBank().isBlank()) ?
          Bank.valueOf(request.getBank()) : account.getBank();

      if (!bank.isMatch(request.getAccountNumber())) {
        throw new InvalidAccountNumberException();
      }

      account.update(request);
    } catch (DataIntegrityViolationException e){
      //수정한 계좌 정보가 이미 목록에 속해있는 경우
      throw new AlreadyExistsAccountException();
    }
  }

  @Override
  @Transactional
  public void deleteAccount(Long userId, Long accountId) {
    //TODO TOKEN 생성 이후엔 CustomUserDetails 로 받을 예정이므로 굳이 불러올 필요 없음
    User user = this.userRepository.findById(userId).orElseThrow(NoUserException::new);
    if (user.getRole() == UserRole.ADMIN) {
      throw new NoAuthorizeException();
    }

    Account account = this.accountRepository.findById(accountId)
        .orElseThrow(NoAccountException::new);

    if (!Objects.equals(userId, account.getUserId())) {
      throw new NoAuthorizeException();
    }

    //대표계좌인 경우 다른 계좌를 대표 계좌로 지정하고 삭제
    //다른 계좌가 없는 경우 오류
    if (account.isRepresentative()) {
      Account nextRepresentativeAccount =
          this.accountRepository.findFirstByUserIdAndIdNotOrderByIdAsc(userId, accountId)
          .orElseThrow(CannotDeleteOnlyOneException::new);

      nextRepresentativeAccount.changeRepresentative();
    }

    this.accountRepository.delete(account);
  }
}
