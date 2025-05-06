// OwnershipTransferResponseDto.java
package rw.rra.management.vehicles.ownership.dtos;

import java.time.LocalDate;

public record OwnershipTransferResponseDto(
        String vehicleChassis,
        String fromOwnerName,
        String toOwnerName,
        String oldPlateNumber,
        String newPlateNumber,
        double transferAmount,
        LocalDate transferDate
) {}
