package rw.rra.management.vehicles.plates.dtos;

import rw.rra.management.vehicles.commons.validation.ValidPlateNumber;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record RegisterPlateNumberDto(

        @NotNull(message = "Plate number is required")
        @ValidPlateNumber
        String plateNumber,
        @NotNull
        String issuedDate
) {}
