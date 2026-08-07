package com.company.notification.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AtLeastOneFieldPresentValidator.class)
public @interface AtLeastOneFieldPresent {

    String message() default "at least one field must be provided";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
