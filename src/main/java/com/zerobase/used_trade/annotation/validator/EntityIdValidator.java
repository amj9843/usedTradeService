package com.zerobase.used_trade.annotation.validator;

import com.zerobase.used_trade.annotation.EntityId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EntityIdValidator implements ConstraintValidator<EntityId, Long> {

  @Override
  public boolean isValid(Long value, ConstraintValidatorContext constraintValidatorContext) {
    if (value == null) return true;

    return value > 0;
  }
}
