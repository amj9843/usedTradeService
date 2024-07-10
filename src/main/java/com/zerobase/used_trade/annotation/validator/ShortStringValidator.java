package com.zerobase.used_trade.annotation.validator;

import com.zerobase.used_trade.annotation.ShortString;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ShortStringValidator implements ConstraintValidator<ShortString, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.isEmpty()) {
      return true;
    }

    return value.trim().length() <= 20;
  }
}
