package kz.narxoz.finalproject.services;

import kz.narxoz.finalproject.dto.request.AdminCreateUserRequestDto;
import kz.narxoz.finalproject.dto.request.UpdateUserRolesRequestDto;
import kz.narxoz.finalproject.dto.response.UserResponseDto;

import java.util.List;

public interface AdminUserService {

    UserResponseDto createUser(AdminCreateUserRequestDto dto);

    UserResponseDto blockUser(Long userId);

    UserResponseDto unblockUser(Long userId);

    UserResponseDto updateUserRoles(Long userId, UpdateUserRolesRequestDto dto);

    UserResponseDto getById(Long userId);

    List<UserResponseDto> getAll();

    void deleteUser(Long userId);

    List<UserResponseDto> getAllUsers();
}
