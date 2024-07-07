package com.zerobase.used_trade.annotation.validator;

import com.zerobase.used_trade.annotation.ValidEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {
   List<Enum<?>> validList;

  @Override
  public void initialize(ValidEnum constraintAnnotation) {
    this.validList = Arrays.asList(
        constraintAnnotation.enumClass().getEnumConstants());
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    boolean isValid = validList.stream().anyMatch(enumValue-> enumValue.name().equals(value));

    if (!isValid) {
      context
          .buildConstraintViolationWithTemplate(
                  context.getDefaultConstraintMessageTemplate() +
                  validList.stream().map(Object::toString).collect(Collectors.joining(", "))
          ).addConstraintViolation()
          .disableDefaultConstraintViolation();
    }

    return isValid;
  }
}
