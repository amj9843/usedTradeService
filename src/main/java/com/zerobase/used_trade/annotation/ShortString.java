package com.zerobase.used_trade.annotation;

import com.zerobase.used_trade.annotation.validator.ShortStringValidator;
import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ShortStringValidator.class)
public @interface ShortString {
  String message() default "{validation.Type.ShortString}";
  Class[] groups() default {};
  Class[] payload() default {};
}
