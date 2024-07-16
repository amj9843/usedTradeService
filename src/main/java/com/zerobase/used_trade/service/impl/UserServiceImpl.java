package com.zerobase.used_trade.service.impl;

import static com.zerobase.used_trade.util.EmailUtility.getDomainFromEmail;

import com.zerobase.used_trade.data.constant.UserRole;
import com.zerobase.used_trade.data.domain.User;
import com.zerobase.used_trade.data.dto.UserDto.Principle;
import com.zerobase.used_trade.data.dto.UserDto.SignInRequest;
import com.zerobase.used_trade.data.dto.UserDto.SignInResponse;
import com.zerobase.used_trade.data.dto.UserDto.SignUpRequest;
import com.zerobase.used_trade.data.dto.UserDto.UpdateRequest;
import com.zerobase.used_trade.exception.impl.AlreadyExistsEmailException;
import com.zerobase.used_trade.exception.impl.ExpiredDomainException;
import com.zerobase.used_trade.exception.impl.IncorrectPasswordOnConfirmException;
import com.zerobase.used_trade.exception.impl.IncorrectPasswordOnSignInException;
import com.zerobase.used_trade.exception.impl.NoPasswordConfirmException;
import com.zerobase.used_trade.exception.impl.NoUserByEmailException;
import com.zerobase.used_trade.exception.impl.NoUserException;
import com.zerobase.used_trade.repository.DomainRepository;
import com.zerobase.used_trade.repository.UserRepository;
import com.zerobase.used_trade.service.UserService;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final DomainRepository domainRepository;

  @Override
  @Transactional
  public Principle signUp(SignUpRequest request) {
    //비밀번호 암호화하여 세팅
    request.setPassword(this.passwordEncoder.encode(request.getPassword()));

    //DB에 저장
    try {
      User user = this.userRepository.save(request.toEntity(
          this.domainRepository.findIdByDomainAddress(getDomainFromEmail(request.getEmail())))
      );

      return Principle.fromEntity(user);
    } catch (DataIntegrityViolationException e) {
      //이메일 중복으로 저장이 되지 않을 시
      throw new AlreadyExistsEmailException();
    }
  }

  @Override
  @Transactional(readOnly = true)
  public SignInResponse signIn(SignInRequest request) {
    User user = this.userRepository.findByEmail(request.getEmail())
        .orElseThrow(NoUserByEmailException::new);

    if (!this.passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new IncorrectPasswordOnSignInException();
    }

    //로그인한 사용자가 관리자일 경우 도메인 만료기간 확인
    if (user.getRole() == UserRole.ADMIN
        && !this.domainRepository.existsByIdAndEndAtAfter(user.getDomainId(), LocalDateTime.now())) {
      throw new ExpiredDomainException();
    }

    /*TODO 토큰 사용 이후
    String token = tokenProvider.generateToken(user);
    return new SignInResponse(token);
     */

    return new SignInResponse(user.getId());
  }

  @Override
  @Transactional
  public void updateUserInfo(Long userId, UpdateRequest request) {
    User user = this.userRepository.findById(userId)
        .orElseThrow(NoUserException::new);

    //비밀번호 변경이 있는 경우 비밀번호 확인이 일치하는지 확인
    if (request.getPassword() == null || request.getPassword().isBlank()) {
      user.update(request);
      return;
    }

    if (request.getPasswordConfirm() == null || request.getPasswordConfirm().isBlank()) {
      throw new NoPasswordConfirmException();
    } else if (!this.passwordEncoder.matches(request.getPasswordConfirm(), user.getPassword())) {
      throw new IncorrectPasswordOnConfirmException();
    }

    request.setPassword(this.passwordEncoder.encode(request.getPassword()));
    user.update(request);
  }
}
