package rw.rra.management.vehicles.plates.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rw.rra.management.vehicles.plates.PlateNumber;
import rw.rra.management.vehicles.plates.dtos.PlateNumberResponseDto;
import rw.rra.management.vehicles.plates.dtos.RegisterPlateNumberDto;
import rw.rra.management.vehicles.owners.Owner;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PlateNumberMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "plateNumber", source = "dto.plateNumber")
    @Mapping(target = "issuedDate", expression = "java(java.time.LocalDate.parse(dto.issuedDate()))")
    @Mapping(target = "owner", source = "owner")

    PlateNumber toEntity(RegisterPlateNumberDto dto, Owner owner);

    @Mapping(target = "plateNumber", source = "plateNumber")
    PlateNumberResponseDto toDto(PlateNumber plateNumber);

    List<PlateNumberResponseDto> toDtoList(List<PlateNumber> plates);
}
