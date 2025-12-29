package kz.narxoz.finalproject.controllers.rest;

import jakarta.validation.Valid;
import kz.narxoz.finalproject.dto.request.ChangePasswordRequestDto;
import kz.narxoz.finalproject.dto.request.UpdateProfileRequestDto;
import kz.narxoz.finalproject.dto.response.UserResponseDto;
import kz.narxoz.finalproject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileRestController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> me() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @PutMapping
    public ResponseEntity<UserResponseDto> updateProfile(@Valid @RequestBody UpdateProfileRequestDto dto) {
        return ResponseEntity.ok(userService.updateProfile(dto));
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequestDto dto) {
        userService.changePassword(dto);
        return ResponseEntity.noContent().build();
    }
}
