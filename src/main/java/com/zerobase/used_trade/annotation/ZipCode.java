package com.zerobase.used_trade.annotation;

import com.zerobase.used_trade.annotation.validator.ZipCodeValidator;
import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ZipCodeValidator.class)
public @interface ZipCode {
  String message() default "{validation.Address.zipCode.Pattern}";
  Class[] groups() default {};
  Class[] payload() default {};
}
