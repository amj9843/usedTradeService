package com.zerobase.used_trade.annotation;

import com.zerobase.used_trade.annotation.validator.PassedTimeValidator;
import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PassedTimeValidator.class)
public @interface PassedTime {
  String message() default "{validation.DateTime.Passed}";
  Class[] groups() default {};
  Class[] payload() default {};
}
