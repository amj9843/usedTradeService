package com.zerobase.used_trade.annotation;

import com.zerobase.used_trade.annotation.validator.TimeStringValidator;
import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TimeStringValidator.class)
public @interface TimeString {
  String message() default "{validation.Pattern.DateTime}";
  Class[] groups() default {};
  Class[] payload() default {};
}
