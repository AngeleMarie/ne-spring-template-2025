// OwnershipTransferRequestDto.java
package rw.rra.management.vehicles.ownership.dtos;


import java.time.LocalDate;

public record OwnershipTransferRequestDto(
        String oldPlateNumber,
        String newPlateNumber,
        Double transferAmount,
        LocalDate transferDate
) {}

