package kz.narxoz.finalproject.controllers;

import jakarta.validation.Valid;
import kz.narxoz.finalproject.dto.request.AdminCreateUserRequestDto;
import kz.narxoz.finalproject.dto.request.UpdateUserRolesRequestDto;
import kz.narxoz.finalproject.dto.response.UserResponseDto;
import kz.narxoz.finalproject.services.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public List<UserResponseDto> getAll() {
        return adminUserService.getAll();
    }

    @GetMapping("/{id}")
    public UserResponseDto getById(@PathVariable Long id) {
        return adminUserService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@Valid @RequestBody AdminCreateUserRequestDto dto) {
        return adminUserService.createUser(dto);
    }

    @PutMapping("/{id}/block")
    public UserResponseDto blockUser(@PathVariable Long id) {
        return adminUserService.blockUser(id);
    }

    @PutMapping("/{id}/unblock")
    public UserResponseDto unblockUser(@PathVariable Long id) {
        return adminUserService.unblockUser(id);
    }

    @PutMapping("/{id}/roles")
    public UserResponseDto updateRoles(@PathVariable Long id, @Valid @RequestBody UpdateUserRolesRequestDto dto) {
        return adminUserService.updateUserRoles(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        adminUserService.deleteUser(id);
    }
}
