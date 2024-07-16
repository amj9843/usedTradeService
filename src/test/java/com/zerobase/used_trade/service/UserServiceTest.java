package com.zerobase.used_trade.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.zerobase.used_trade.data.constant.UserRole;
import com.zerobase.used_trade.data.domain.User;
import com.zerobase.used_trade.data.dto.UserDto.Principle;
import com.zerobase.used_trade.data.dto.UserDto.SignInRequest;
import com.zerobase.used_trade.data.dto.UserDto.SignInResponse;
import com.zerobase.used_trade.data.dto.UserDto.SignUpRequest;
import com.zerobase.used_trade.data.dto.UserDto.UpdateRequest;
import com.zerobase.used_trade.repository.DomainRepository;
import com.zerobase.used_trade.repository.UserRepository;
import com.zerobase.used_trade.service.impl.UserServiceImpl;
import java.util.Optional;
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

  @DisplayName("로그인")
  @Test
  void signIn() {
    //given
    SignUpRequest signUpDto = new SignUpRequest(
        email, password, name, nickName, phoneNumber
    );

    SignInRequest signInDto = new SignInRequest(
        email, password
    );

    given(userRepository.findByEmail(anyString())).willReturn(Optional.of(signUpDto.toEntity(1L)));
    given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
    given(domainRepository.existsByIdAndEndAtAfter(anyLong(), any())).willReturn(true);

    //when
    SignInResponse response = userService.signIn(signInDto);

    //then
    assertThat(response.getId()).isEqualTo(null);
  }

  @DisplayName("유저 정보 변경")
  @Test
  void updateUserInfo() {
    //given
    Long userId = 1L;

    SignUpRequest userDto = new SignUpRequest(
        email, password, name, nickName, phoneNumber
    );
    UpdateRequest updateDto = new UpdateRequest(
        "changePassword", "correctPassword", "changeName", "", ""
    );

    User user = userDto.toEntity(null);

    given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(user));
    given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
    given(passwordEncoder.encode(updateDto.getPassword())).willReturn(updateDto.getPassword());

    //when
    userService.updateUserInfo(userId, updateDto);

    //then
    verify(userRepository, times(1)).findById(userId);

    assertThat(user.getPassword()).isEqualTo(passwordEncoder.encode("changePassword"));
    assertThat(user.getName()).isEqualTo("changeName");
  }
}
