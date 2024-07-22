package com.zerobase.used_trade.annotation.validator;

import static com.zerobase.used_trade.util.DateTimeUtility.stringToLocalDateTime;

import com.zerobase.used_trade.annotation.TimeString;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TimeStringValidator implements ConstraintValidator<TimeString, String> {
  @Override
  public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
    if (s == null || s.isEmpty()) return true;

    LocalDateTime checkTime = stringToLocalDateTime(s);
    return checkTime != null;
  }
}
