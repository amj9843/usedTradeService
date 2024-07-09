package com.zerobase.used_trade.annotation;

import com.zerobase.used_trade.annotation.validator.DomainAddressValidator;
import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DomainAddressValidator.class)
public @interface DomainAddress {
  String message() default "{validation.Domain.address.Pattern}";
  Class[] groups() default {};
  Class[] payload() default {};
}
