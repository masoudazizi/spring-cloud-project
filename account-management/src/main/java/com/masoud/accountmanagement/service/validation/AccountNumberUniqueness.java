package com.masoud.accountmanagement.service.validation;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Constraint(validatedBy = {AccountNumberValidator.class})
@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AccountNumberUniqueness {
    String message() default "account number must be unique";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
