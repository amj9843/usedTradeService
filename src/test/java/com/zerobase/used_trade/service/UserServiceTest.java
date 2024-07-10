package com.zerobase.used_trade.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.zerobase.used_trade.data.constant.UserRole;
import com.zerobase.used_trade.data.domain.User;
import com.zerobase.used_trade.data.dto.UserDto.Principle;
import com.zerobase.used_trade.data.dto.UserDto.SignUpRequest;
import com.zerobase.used_trade.repository.DomainRepository;
import com.zerobase.used_trade.repository.UserRepository;
import com.zerobase.used_trade.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
  @InjectMocks
  private UserServiceImpl userService;
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private UserRepository userRepository;
  @Mock
  private DomainRepository domainRepository;

  public static String email = "user1@test.com";
  public static String password = "password12345?!";
  public static String name = "realName";
  public static String nickName = "nickName";
  public static String phoneNumber = null;

  @DisplayName("회원 가입")
  @Test
  void signUp() {
    //given
    Long domainId = 1L;

    SignUpRequest dto = new SignUpRequest(
        email, password, name, nickName, phoneNumber
    );

    given(userRepository.existsByEmail(anyString())).willReturn(false);
    given(domainRepository.findIdByDomainAddress(anyString())).willReturn(domainId);
    given(userRepository.save(any())).willReturn(dto.toEntity(domainId));

    //when
    Principle user = userService.signUp(dto);
    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

    //then
    verify(userRepository, times(1)).save(userCaptor.capture());

    assertThat(userCaptor.getValue().getEmail()).isEqualTo(email);
    assertThat(userCaptor.getValue().getPassword()).isEqualTo(passwordEncoder.encode(dto.getPassword()));
    assertThat(userCaptor.getValue().getRole()).isEqualTo(UserRole.ADMIN);
    assertThat(user.getEmail()).isEqualTo(email);
    assertThat(user.getRole()).isEqualTo(UserRole.ADMIN);
  }
}
