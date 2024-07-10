package com.zerobase.used_trade.validator;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.zerobase.used_trade.data.dto.UserDto.SignUpRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserDtoValidationTest {
  private static Validator validator;

  @BeforeEach
  void setUpValidator() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @DisplayName("UserDto.SignUpRequest 유효성 검사")
  @Test
  void signUpRequestValidation() {
    //given
    SignUpRequest dto =
        new SignUpRequest(
            "", "", null, "  ", "080");

    //when
    Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(dto);

    //then
    for (ConstraintViolation<SignUpRequest> violation: violations) {
      switch (violation.getPropertyPath().toString()) {
        case "email":
          if (dto.getEmail().isEmpty()) {
            assertThat(violation.getMessage()).isEqualTo("이메일 입력은 필수로, 빈 칸으로 입력할 수 없습니다.");
          } else {
            assertThat(violation.getMessage()).isEqualTo("이메일 입력 형식이 잘못되었습니다. 아이디@도메인주소 형식으로 입력되어있는지 확인해주세요.");
          }
          break;
        case "password":
          if (dto.getPassword().isEmpty()) {
            assertThat(violation.getMessage()).isEqualTo("비밀번호 입력은 필수로, 빈 칸으로 입력할 수 없습니다.");
          } else {
            assertThat(violation.getMessage()).isEqualTo("비밀번호 입력 형식이 잘못되었습니다. 비밀번호는 8자 이상의 영문자와 특수기호, 숫자의 조합이어야 합니다.");
          }
          break;
        case "name":
          if (dto.getName() == null || dto.getName().isBlank()) {
            assertThat(violation.getMessage()).isEqualTo("사용자명 입력은 필수로, 빈 칸으로만 이루어질 수 없습니다.");
          } else {
            assertThat(violation.getMessage()).isEqualTo("문자열은 양끝의 공백을 제외하고, 공백포함 20글자 이내여야 합니다.");
          }
          break;
        case "nickName":
          if (dto.getNickName() == null || dto.getNickName().isBlank()) {
            assertThat(violation.getMessage()).isEqualTo("닉네임 입력은 필수로, 빈 칸으로만 이루어질 수 없습니다.");
          } else {
            assertThat(violation.getMessage()).isEqualTo("문자열은 양끝의 공백을 제외하고, 공백포함 20글자 이내여야 합니다.");
          }
          break;
        case "phoneNumber":
          assertThat(violation.getMessage()).isEqualTo("전화번호 입력 형식이 잘못되었습니다. 전화번호는 000-0000-0000 형식으로 입력되어야 합니다.");
          break;
        default:
          break;
      }
    }
    assertThat(violations.size()).isEqualTo(5); //틀리게 입력한 DTO 내 항목 개수
  }

  /*TODO UserDto.UpdateRequest 유효성 검사
  @DisplayName("UserDto.UpdateRequest 유효성 검사")
  @Test
  void updateRequestValidation() {
    //given
    UpdateRequest dto =
        new UpdateRequest("  ", "", " ", "  ", "00");

    //when
    Set<ConstraintViolation<UpdateRequest>> violations = validator.validate(dto);

    //then
    for (ConstraintViolation<UpdateRequest> violation: violations) {
      switch (violation.getPropertyPath().toString()) {
        case "password":
          assertThat(violation.getMessage()).isEqualTo("비밀번호 입력 형식이 잘못되었습니다. 비밀번호는 8자 이상의 영문자와 특수기호, 숫자의 조합이어야 합니다.");
          break;
        case "name":
          if (dto.getName().isBlank()) {
            assertThat(violation.getMessage()).isEqualTo("미입력(null), 공백은 입력 가능하나 빈 칸으로만 이루어진 값은 입력할 수 없습니다.");
          } else {
            assertThat(violation.getMessage()).isEqualTo("문자열은 양끝의 공백을 제외하고, 공백포함 20글자 이내여야 합니다.");
          }
          break;
        case "nickName":
          if (dto.getNickName().isBlank()) {
            assertThat(violation.getMessage()).isEqualTo("미입력(null), 공백은 입력 가능하나 빈 칸으로만 이루어진 값은 입력할 수 없습니다.");
          } else {
            assertThat(violation.getMessage()).isEqualTo("문자열은 양끝의 공백을 제외하고, 공백포함 20글자 이내여야 합니다.");
          }
          break;
        case "phoneNumber":
          assertThat(violation.getMessage()).isEqualTo("전화번호 입력 형식이 잘못되었습니다. 전화번호는 000-0000-0000 형식으로 입력되어야 합니다.");
          break;
        default:
          break;
      }
    }
    assertThat(violations.size()).isEqualTo(4); //틀리게 입력한 DTO 내 항목 개수
  }
   */
}
