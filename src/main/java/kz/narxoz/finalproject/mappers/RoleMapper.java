package kz.narxoz.finalproject.mappers;

import kz.narxoz.finalproject.dto.response.RoleResponseDto;
import kz.narxoz.finalproject.models.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleResponseDto toDto(Role role);
}
