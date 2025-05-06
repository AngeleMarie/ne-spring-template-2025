package rw.rra.management.vehicles.vehicles.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import rw.rra.management.vehicles.commons.validation.ValidPlateNumber;

import java.util.UUID;

public record VehicleRequestDto(
        @NotBlank String brand,
                                @NotBlank String model,
                                @NotBlank String color,
                                @NotBlank String chassisNumber,
                                @NotNull String manufacturerCompany,
                                @NotNull int manufactureYear,
                                @NotNull double price
) {}
