// OwnershipTransferMapper.java
package rw.rra.management.vehicles.ownership.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rw.rra.management.vehicles.ownership.OwnershipTransfer;
import rw.rra.management.vehicles.ownership.dtos.OwnershipTransferResponseDto;

@Mapper(componentModel = "spring")
public interface OwnershipTransferMapper {

    @Mapping(source = "vehicle.chassisNumber", target = "vehicleChassis")
    @Mapping(source = "fromOwner.firstName", target = "fromOwnerName")
    @Mapping(source = "toOwner.firstName", target = "toOwnerName")
    @Mapping(source = "oldPlate.plateNumber", target = "oldPlateNumber")
    @Mapping(source = "newPlate.plateNumber", target = "newPlateNumber")
    OwnershipTransferResponseDto toDto(OwnershipTransfer transfer);
}
