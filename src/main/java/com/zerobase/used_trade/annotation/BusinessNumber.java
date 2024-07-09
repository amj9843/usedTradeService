package com.zerobase.used_trade.annotation;

import com.zerobase.used_trade.annotation.validator.BusinessNumberValidator;
import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BusinessNumberValidator.class)
public @interface BusinessNumber {
  String message() default "{validation.Company.businessNumber.Pattern}";
  Class[] groups() default {};
  Class[] payload() default {};
}
