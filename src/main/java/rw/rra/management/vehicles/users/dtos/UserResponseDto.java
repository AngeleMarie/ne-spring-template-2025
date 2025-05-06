package rw.rra.management.vehicles.users.dtos;


import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String firstName,
        String lastName,
        String email
) {
}
