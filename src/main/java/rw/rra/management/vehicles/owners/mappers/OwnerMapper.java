package rw.rra.management.vehicles.owners.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import rw.rra.management.vehicles.owners.Address;
import rw.rra.management.vehicles.owners.Owner;
import rw.rra.management.vehicles.owners.dtos.OwnerRequestDto;
import rw.rra.management.vehicles.owners.dtos.OwnerResponseDto;

@Mapper(componentModel = "spring")
public interface OwnerMapper {

    @Mappings({
            @Mapping(source = "province", target = "address.province"),
            @Mapping(source = "district", target = "address.district"),
            @Mapping(source = "street", target = "address.street")
    })
    Owner toEntity(OwnerRequestDto dto);

    @Mappings({
            @Mapping(source = "address.province", target = "province"),
            @Mapping(source = "address.district", target = "district"),
            @Mapping(source = "address.street", target = "street")
    })
    OwnerResponseDto toDto(Owner entity);
}
