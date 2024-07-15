package com.zerobase.used_trade.controller;

import static java.lang.String.format;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.used_trade.data.constant.SuccessCode;
import com.zerobase.used_trade.data.constant.UserRole;
import com.zerobase.used_trade.data.dto.UserDto.Principle;
import com.zerobase.used_trade.data.dto.UserDto.SignInRequest;
import com.zerobase.used_trade.data.dto.UserDto.SignInResponse;
import com.zerobase.used_trade.data.dto.UserDto.SignUpRequest;
import com.zerobase.used_trade.data.dto.UserDto.UpdateRequest;
import com.zerobase.used_trade.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
public class UserControllerTest {
  @Autowired
  private MockMvc mvc;
  @MockBean
  private UserService userService;
  @Autowired
  private ObjectMapper objectMapper;

  public static String URL = "/user";

  public static String email = "user1@test.com";
  public static String password = "password12345?!";
  public static String name = "realName";
  public static String nickName = "nickName";
  public static String phoneNumber = null;

  @DisplayName("회원 가입")
  @WithMockUser
  @Test
  void signUp() throws Exception {
    //given
    SignUpRequest dto = new SignUpRequest(
        email, password, name, nickName, phoneNumber
    );

    //when
    when(userService.signUp(any())).thenReturn(Principle.fromEntity(dto.toEntity(1L)));

    //then
    mvc.perform(
            post(format("%s/sign-up", URL))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf())
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(SuccessCode.CREATED_SUCCESS.status().value()))
        .andExpect(jsonPath("$.data.email").value(email))
        .andExpect(jsonPath("$.data.role").value(UserRole.ADMIN.name()));
  }
  @DisplayName("로그인")
  @WithMockUser
  @Test
  void signIn() throws Exception {
    //given
    SignInRequest dto = new SignInRequest(
        email, password
    );

    //when
    when(userService.signIn(any())).thenReturn(new SignInResponse(1L));

    //then
    mvc.perform(
        post(format("%s/sign-in", URL))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto))
            .with(csrf())
    )
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.code").value(SuccessCode.SIGN_IN_SUCCESS.status().value()))
    .andExpect(jsonPath("$.data.id").value(1L));
  }

  @DisplayName("사용자 정보 변경")
  @WithMockUser
  @Test
  void updateUserInfo() throws Exception {
    //given
    Long userId = 1L;
    UpdateRequest dto = new UpdateRequest(
        null, null, "changeName", null, null
    );

    doNothing().when(userService).updateUserInfo(anyLong(), any());
    //then
    mvc.perform(
            patch(format("%s", URL))
                .header("Authorization", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf())
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(SuccessCode.UPDATED_SUCCESS.status().value()))
        .andExpect(jsonPath("$.data").doesNotExist());
  }
}
