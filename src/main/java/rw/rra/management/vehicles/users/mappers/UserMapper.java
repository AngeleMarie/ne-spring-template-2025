package rw.rra.management.vehicles.users.mappers;



import rw.rra.management.vehicles.auth.dtos.RegisterRequestDto;
import rw.rra.management.vehicles.users.User;
import rw.rra.management.vehicles.users.dtos.UserResponseDto;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface UserMapper {
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    User toEntity(RegisterRequestDto userDto);
    UserResponseDto toResponseDto(User user);
}

