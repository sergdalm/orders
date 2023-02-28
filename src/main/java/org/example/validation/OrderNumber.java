package org.example.validation;

import org.example.validation.impl.OrderNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = OrderNumberValidator.class)
@Retention(RUNTIME)
@Target(FIELD)
public @interface OrderNumber {

    String message() default "Order number is invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
