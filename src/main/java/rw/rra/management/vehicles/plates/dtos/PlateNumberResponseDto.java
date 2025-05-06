package rw.rra.management.vehicles.plates.dtos;

import java.util.UUID;

public record PlateNumberResponseDto(
        UUID id,
        String plateNumber

) {}

