package rw.rra.management.vehicles.auth.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record InitiatePasswordResetDto(
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid.")
        String email
) {
}
