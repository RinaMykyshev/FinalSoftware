package kz.narxoz.finalproject.controllers;

import jakarta.validation.Valid;
import kz.narxoz.finalproject.dto.request.ChangePasswordRequestDto;
import kz.narxoz.finalproject.dto.request.UpdateProfileRequestDto;
import kz.narxoz.finalproject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProfile(@Valid @RequestBody UpdateProfileRequestDto dto) {
        userService.updateProfile(dto);
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@Valid @RequestBody ChangePasswordRequestDto dto) {
        userService.changePassword(dto);
    }
}
