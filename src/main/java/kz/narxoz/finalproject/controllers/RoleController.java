package kz.narxoz.finalproject.controllers;

import jakarta.validation.Valid;
import kz.narxoz.finalproject.dto.request.RoleRequestDto;
import kz.narxoz.finalproject.dto.response.RoleResponseDto;
import kz.narxoz.finalproject.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoleResponseDto create(@Valid @RequestBody RoleRequestDto dto) {
        return roleService.create(dto);
    }

    @GetMapping("/{id}")
    public RoleResponseDto getById(@PathVariable Long id) {
        return roleService.getById(id);
    }

    @GetMapping
    public List<RoleResponseDto> getAll() {
        return roleService.getAll();
    }

    @PutMapping("/{id}")
    public RoleResponseDto update(@PathVariable Long id, @Valid @RequestBody RoleRequestDto dto) {
        return roleService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        roleService.delete(id);
    }
}
