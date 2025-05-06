package rw.rra.management.vehicles.owners.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import rw.rra.management.vehicles.commons.validation.ValidPlateNumber;
import rw.rra.management.vehicles.commons.validation.ValidRwandaId;
import rw.rra.management.vehicles.commons.validation.ValidRwandanPhoneNumber;

public record OwnerRequestDto(
        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @ValidRwandaId
        @NotBlank(message = "National ID is required")
        String nationalId,

        @ValidRwandanPhoneNumber
        @NotBlank(message = "Phone number is required")
        String phoneNumber,

        @Email(message = "Email should be valid")
        String email,

        @NotBlank(message = "Province is required")
        String province,

        @NotBlank(message = "District is required")
        String district,

        @NotBlank(message = "Street is required")
        String street
) {}
