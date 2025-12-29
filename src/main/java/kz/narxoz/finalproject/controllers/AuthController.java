package kz.narxoz.finalproject.controllers;

import jakarta.validation.Valid;
import kz.narxoz.finalproject.dto.request.LoginRequestDto;
import kz.narxoz.finalproject.dto.request.RegisterRequestDto;
import kz.narxoz.finalproject.dto.response.UserResponseDto;
import kz.narxoz.finalproject.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto register(@Valid @RequestBody RegisterRequestDto dto) {
        return authService.register(dto);
    }

    @PostMapping("/login")
    public UserResponseDto login(@Valid @RequestBody LoginRequestDto dto) {
        return authService.login(dto);
    }
}
