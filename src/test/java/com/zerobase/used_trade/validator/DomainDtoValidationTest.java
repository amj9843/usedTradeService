package com.zerobase.used_trade.validator;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.zerobase.used_trade.data.dto.DomainDto.EnrollRequest;
import com.zerobase.used_trade.data.dto.DomainDto.ExtensionRequest;
import com.zerobase.used_trade.data.dto.DomainDto.UpdateRequest;
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
public class DomainDtoValidationTest {
  private static Validator validator;

  @BeforeEach
  void setUpValidator() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @DisplayName("DomainDto.EnrollRequest 유효성 검사")
  @Test
  void enrollRequestValidation() {
    //given
    EnrollRequest dto =
        new EnrollRequest(
            "domain",
            "  ",
            "1111-11-11111",
            "123456",
            "  ",
            "  ",
            "   ",
            "0123456789",
            "잘못된값");

    //when
    Set<ConstraintViolation<EnrollRequest>> violations = validator.validate(dto);

    //then
    for (ConstraintViolation<EnrollRequest> violation: violations) {
      switch (violation.getPropertyPath().toString()) {
        case "domainAddress":
          if (dto.getDomainAddress().isEmpty()) {
            assertThat(violation.getMessage()).isEqualTo("도메인 입력은 필수로, 빈 칸으로 입력할 수 없습니다.");
          } else {
            assertThat(violation.getMessage()).isEqualTo("도메인 형식이 잘못되었습니다.");
          }
          break;
        case "companyName":
          assertThat(violation.getMessage()).isEqualTo("회사명은 필수로, 빈 칸으로만 이루어질 수 없습니다.");
          break;
        case "businessNumber":
          if (dto.getBusinessNumber().isEmpty()) {
            assertThat(violation.getMessage()).isEqualTo("사업자 번호 입력은 필수로, 빈 칸으로 입력할 수 없습니다.");
          } else {
            assertThat(violation.getMessage()).isEqualTo("사업자 번호 형식이 잘못되었습니다. 사업자 번호는 000-00-00000 형식으로 입력되어야 합니다.");
          }
          break;
        case "zipCode":
          if (dto.getZipCode().isEmpty()) {
            assertThat(violation.getMessage()).isEqualTo("우편번호 입력은 필수로, 빈 칸으로 입력할 수 없습니다.");
          } else {
            assertThat(violation.getMessage()).isEqualTo("우편번호 입력 형식이 잘못되었습니다. 우편번호는 숫자 5자리로만 입력되어야 합니다.");
          }
          break;
        case "roadAddress":
          assertThat(violation.getMessage()).isEqualTo("도로명 주소 입력은 필수로, 빈 칸으로만 이루어질 수 없습니다.");
          break;
        case "address":
          assertThat(violation.getMessage()).isEqualTo("지번 입력은 필수로, 빈 칸으로만 이루어질 수 없습니다.");
          break;
        case "detail":
          assertThat(violation.getMessage()).isEqualTo("미입력(null), 공백은 입력 가능하나 빈 칸으로만 이루어진 값은 입력할 수 없습니다.");
          break;
        case "phoneNumber":
          if (dto.getPhoneNumber().isEmpty()) {
            assertThat(violation.getMessage()).isEqualTo("회사 전화번호 입력은 필수로, 빈 칸으로 입력할 수 없습니다.");
          } else {
            assertThat(violation.getMessage()).isEqualTo("전화번호 입력 형식이 잘못되었습니다. 전화번호는 000-0000-0000 형식으로 입력되어야 합니다.");
          }
          break;
        case "period":
          if (dto.getPeriod().isEmpty()) {
            assertThat(violation.getMessage()).isEqualTo("도메인의 구독 종류 입력은 필수로, 빈 칸으로 입력할 수 없습니다.");
          } else {
            assertThat(violation.getMessage()).contains("지정된 항목 중 하나를 입력해야 합니다. 입력 가능 항목(설명): ");
          }
        default:
          break;
      }
    }
    assertThat(violations.size()).isEqualTo(9); //틀리게 입력한 DTO 내 항목 개수
  }

  @DisplayName("DomainDto.ExtensionRequest 유효성 검사")
  @Test
  void extensionRequestValidation() {
    //given
    ExtensionRequest dto =
        new ExtensionRequest("");

    //when
    Set<ConstraintViolation<ExtensionRequest>> violations = validator.validate(dto);

    //then
    for (ConstraintViolation<ExtensionRequest> violation: violations) {
      assertThat(violation.getPropertyPath().toString()).isEqualTo("period");
      if (dto.getPeriod() == null || dto.getPeriod().isEmpty()) {
        assertThat(violation.getMessage()).isEqualTo("도메인의 구독 종류 입력은 필수로, 빈 칸으로 입력할 수 없습니다.");
      } else {
        assertThat(violation.getMessage()).contains("지정된 항목 중 하나를 입력해야 합니다. 입력 가능 항목(설명): ");
      }
    }
    assertThat(violations.size()).isEqualTo(1);
  }

  @DisplayName("DomainDto.UpdateRequest 유효성 검사")
  @Test
  void updateRequestValidation() {
    //given
    UpdateRequest dto =
        new UpdateRequest(
            "",
            "",
            "",
            "",
            "",
            "",
            " ",
            "  "
        );

    //when
    Set<ConstraintViolation<UpdateRequest>> violations = validator.validate(dto);

    //then
    for (ConstraintViolation<UpdateRequest> violation: violations) {
      switch (violation.getPropertyPath().toString()) {
        case "domainAddress":
          assertThat(violation.getMessage()).isEqualTo("도메인 형식이 잘못되었습니다.");
          break;
        case "companyName":
          assertThat(violation.getMessage()).isEqualTo("회사명은 필수로, 빈 칸으로만 이루어질 수 없습니다.");
          break;
        case "businessNumber":
          assertThat(violation.getMessage()).isEqualTo("사업자 번호 형식이 잘못되었습니다. 사업자 번호는 000-00-00000 형식으로 입력되어야 합니다.");
          break;
        case "zipCode":
          assertThat(violation.getMessage()).isEqualTo("우편번호 입력 형식이 잘못되었습니다. 우편번호는 숫자 5자리로만 입력되어야 합니다.");
          break;
        case "roadAddress":
          assertThat(violation.getMessage()).isEqualTo("도로명 주소 입력은 필수로, 빈 칸으로만 이루어질 수 없습니다.");
          break;
        case "address":
          assertThat(violation.getMessage()).isEqualTo("지번 입력은 필수로, 빈 칸으로만 이루어질 수 없습니다.");
          break;
        case "detail":
          assertThat(violation.getMessage()).isEqualTo("미입력(null), 공백은 입력 가능하나 빈 칸으로만 이루어진 값은 입력할 수 없습니다.");
          break;
        case "phoneNumber":
          assertThat(violation.getMessage()).isEqualTo("전화번호 입력 형식이 잘못되었습니다. 전화번호는 000-0000-0000 형식으로 입력되어야 합니다.");
          break;
        default:
          break;
      }
    }
    assertThat(violations.size()).isEqualTo(2);
  }
}
