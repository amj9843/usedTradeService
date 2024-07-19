package com.zerobase.used_trade.annotation;

import com.zerobase.used_trade.annotation.validator.MeetingTimeValidator;
import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MeetingTimeValidator.class)
public @interface MeetingTime {
  String message() default "{validation.Meeting.DateTime}";
  Class[] groups() default {};
  Class[] payload() default {};
}
