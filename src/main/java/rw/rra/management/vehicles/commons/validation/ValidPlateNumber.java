package rw.rra.management.vehicles.commons.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = PlateNumberValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPlateNumber {
    String message() default "Invalid Rwanda Vehicle Plate Number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

