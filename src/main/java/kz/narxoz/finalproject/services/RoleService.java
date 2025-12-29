package kz.narxoz.finalproject.services;

import kz.narxoz.finalproject.dto.request.RoleRequestDto;
import kz.narxoz.finalproject.dto.response.RoleResponseDto;

import java.util.List;

public interface RoleService {
    RoleResponseDto create(RoleRequestDto dto);

    RoleResponseDto getById(Long id);

    List<RoleResponseDto> getAll();

    RoleResponseDto update(Long id, RoleRequestDto dto);

    void delete(Long id);
}
