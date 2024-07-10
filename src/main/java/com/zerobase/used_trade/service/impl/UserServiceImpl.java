package com.zerobase.used_trade.service.impl;

import com.zerobase.used_trade.data.domain.User;
import com.zerobase.used_trade.data.dto.UserDto.Principle;
import com.zerobase.used_trade.data.dto.UserDto.SignUpRequest;
import com.zerobase.used_trade.exception.impl.AlreadyExistsEmailException;
import com.zerobase.used_trade.repository.DomainRepository;
import com.zerobase.used_trade.repository.UserRepository;
import com.zerobase.used_trade.service.UserService;
import lombok.AllArgsConstructor;
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
    //아이디 중복 확인
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new AlreadyExistsEmailException();
    }

    //비밀번호 암호화하여 세팅
    request.setPassword(this.passwordEncoder.encode(request.getPassword()));

    //DB에 저장
    User user = userRepository.save(request.toEntity(
        this.domainRepository.findIdByDomainAddress(request.getEmail().substring(
            request.getEmail().lastIndexOf("@")+1)))
    );

    return Principle.fromEntity(user);
  }
}
