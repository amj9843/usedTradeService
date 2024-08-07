package com.zerobase.used_trade.annotation;

import com.zerobase.used_trade.annotation.validator.PasswordValidator;
import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface Password {
  String message() default "{validation.Pattern.Password}";
  Class[] groups() default {};
  Class[] payload() default {};
}
