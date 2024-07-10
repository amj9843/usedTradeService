package com.zerobase.used_trade.annotation.validator;

import com.zerobase.used_trade.annotation.Password;
import com.zerobase.used_trade.data.constant.PatternType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.isEmpty()) {
      return true;
    }

    return value.matches(PatternType.PASSWORD.regex());
  }
}
