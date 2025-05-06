package rw.rra.management.vehicles.commons.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RwandaNationalIdValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRwandaId {
    String message() default "Invalid Rwanda National ID";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}