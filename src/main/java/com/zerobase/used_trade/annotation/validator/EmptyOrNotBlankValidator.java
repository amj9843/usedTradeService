package com.zerobase.used_trade.annotation.validator;

import com.zerobase.used_trade.annotation.EmptyOrNotBlank;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmptyOrNotBlankValidator implements ConstraintValidator<EmptyOrNotBlank, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.isEmpty()) {
      return true;
    }

    return !value.isBlank();
  }
}
