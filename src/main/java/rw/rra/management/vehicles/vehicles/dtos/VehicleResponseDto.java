package rw.rra.management.vehicles.vehicles.dtos;

import java.util.UUID;

public record VehicleResponseDto(
        UUID id,
        String brand,
        String model,
        String color,
        String chassisNumber,
        String plateNumber,
        String ownerFirstName,
        String ownerLastName,
        String ownerNationalId
) {}
