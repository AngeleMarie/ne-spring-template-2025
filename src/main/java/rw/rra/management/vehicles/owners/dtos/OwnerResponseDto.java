package rw.rra.management.vehicles.owners.dtos;

import java.util.UUID;

public record OwnerResponseDto(
        UUID id,
        String firstName,
        String lastName,
        String nationalId,
        String phoneNumber,
        String email,
        String province,
        String district,
        String street
) {}

