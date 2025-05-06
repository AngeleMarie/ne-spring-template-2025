package rw.rra.management.vehicles.vehicles.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import rw.rra.management.vehicles.plates.PlateNumber;
import rw.rra.management.vehicles.vehicles.Vehicle;
import rw.rra.management.vehicles.vehicles.dtos.VehicleRequestDto;
import rw.rra.management.vehicles.vehicles.dtos.VehicleResponseDto;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "modelName", source = "model"),
            @Mapping(target = "brand", source = "brand"),
            @Mapping(target = "color", source = "color"),
            @Mapping(target = "manufacturerCompany", source = "manufacturerCompany"),
            @Mapping(target = "manufactureYear", expression = "java(java.time.Year.of(dto.manufactureYear()))"),
            @Mapping(target = "price", source = "price"),
            @Mapping(target = "chassisNumber", source = "chassisNumber"),

    })
    Vehicle toEntity(VehicleRequestDto dto);

    @Mappings({
            @Mapping(source = "brand", target = "brand"),
            @Mapping(source = "color", target = "color"),
            @Mapping(source = "modelName", target = "model"),
            @Mapping(source = "chassisNumber", target = "chassisNumber"),
            @Mapping(source = "plateNumber", target = "plateNumber"),
            @Mapping(source = "owner.firstName", target = "ownerFirstName"),
            @Mapping(source = "owner.lastName", target = "ownerLastName"),
            @Mapping(source = "owner.nationalId", target = "ownerNationalId")
    })
    VehicleResponseDto toDto(Vehicle vehicle);


    default PlateNumber map(String plateNumberStr) {
        if (plateNumberStr == null || plateNumberStr.isBlank()) return null;
        PlateNumber plateNumber = new PlateNumber();
        plateNumber.setPlateNumber(plateNumberStr);
        return plateNumber;
    }

    default String map(PlateNumber plateNumber) {
        return plateNumber != null ? plateNumber.getPlateNumber() : null;
    }
}
