package com.zerobase.used_trade.annotation;

import com.zerobase.used_trade.annotation.validator.PhoneNumberValidator;
import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
public @interface PhoneNumber {
  String message() default "{validation.Pattern.PhoneNumber}";
  Class[] groups() default {};
  Class[] payload() default {};
}
