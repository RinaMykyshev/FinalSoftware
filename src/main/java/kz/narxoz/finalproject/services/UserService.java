package kz.narxoz.finalproject.services;

import kz.narxoz.finalproject.dto.request.ChangePasswordRequestDto;
import kz.narxoz.finalproject.dto.request.UpdateProfileRequestDto;
import kz.narxoz.finalproject.dto.response.UserResponseDto;

public interface UserService {
    void updateProfile(UpdateProfileRequestDto dto);
    void changePassword(ChangePasswordRequestDto dto);

    UserResponseDto getCurrentUser();
}
