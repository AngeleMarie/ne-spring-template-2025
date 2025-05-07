package rw.rra.management.vehicles.vehicles.dtos;

import jakarta.validation.constraints.NotBlank;
import rw.rra.management.vehicles.commons.validation.ValidPlateNumber;

import java.util.UUID;

public record AssignPlateAndOwnerRequest(
        @ValidPlateNumber
        @NotBlank
        String plateNumber
) {}
