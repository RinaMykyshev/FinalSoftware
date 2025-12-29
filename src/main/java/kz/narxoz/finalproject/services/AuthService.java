package kz.narxoz.finalproject.services;

import kz.narxoz.finalproject.dto.request.LoginRequestDto;
import kz.narxoz.finalproject.dto.request.RegisterRequestDto;
import kz.narxoz.finalproject.dto.response.UserResponseDto;

public interface AuthService {
    UserResponseDto register(RegisterRequestDto dto);
    UserResponseDto login(LoginRequestDto dto);
}
