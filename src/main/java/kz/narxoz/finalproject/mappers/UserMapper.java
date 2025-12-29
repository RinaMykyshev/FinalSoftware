package kz.narxoz.finalproject.mappers;

import kz.narxoz.finalproject.dto.response.UserResponseDto;
import kz.narxoz.finalproject.models.Role;
import kz.narxoz.finalproject.models.User;
import org.mapstruct.Mapper;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto toDto(User user);

    default Set<String> mapRoles(Set<Role> roles) {
        if (roles == null) {
            return Set.of();
        }
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }
}
