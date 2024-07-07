package com.zerobase.used_trade.annotation;

import com.zerobase.used_trade.annotation.validator.EnumValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EnumValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEnum {
  String message() default "{validation.Enum}";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
  Class<? extends java.lang.Enum<?>> enumClass();
}
