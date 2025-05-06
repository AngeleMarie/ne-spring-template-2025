package rw.rra.management.vehicles.commons.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PlateNumberValidator implements ConstraintValidator<ValidPlateNumber, String> {

    // Regular expression for validating the plate number format
    private static final String PLATE_NUMBER_PATTERN = "^RA[A-Z]{1}[0-9]{3}[A-Z]{1}$";
    private static final Pattern PATTERN = Pattern.compile(PLATE_NUMBER_PATTERN);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return false;
        }

        // Remove spaces and check the format
        value = value.replaceAll("\\s", "");

        // Check if the plate number matches the pattern
        Matcher matcher = PATTERN.matcher(value);
        return matcher.matches();
    }
}
