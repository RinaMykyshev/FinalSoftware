package kz.narxoz.finalproject.services.impl;

import kz.narxoz.finalproject.dto.request.RoleRequestDto;
import kz.narxoz.finalproject.dto.response.RoleResponseDto;
import kz.narxoz.finalproject.exception.BadRequestException;
import kz.narxoz.finalproject.exception.NotFoundException;
import kz.narxoz.finalproject.mappers.RoleMapper;
import kz.narxoz.finalproject.models.Role;
import kz.narxoz.finalproject.repositories.RoleRepository;
import kz.narxoz.finalproject.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public RoleResponseDto create(RoleRequestDto dto) {
        String roleName = normalize(dto);

        if (roleRepository.findByName(roleName).isPresent()) {
            throw new BadRequestException("Role already exists: " + roleName);
        }

        Role role = new Role();
        role.setName(roleName);

        return roleMapper.toDto(roleRepository.save(role));
    }

    @Override
    public RoleResponseDto getById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Role not found"));
        return roleMapper.toDto(role);
    }

    @Override
    public List<RoleResponseDto> getAll() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toDto)
                .toList();
    }

    @Override
    public RoleResponseDto update(Long id, RoleRequestDto dto) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Role not found"));

        String roleName = normalize(dto);

        roleRepository.findByName(roleName)
                .filter(r -> !r.getId().equals(id))
                .ifPresent(r -> {
                    throw new BadRequestException("Role name already in use: " + roleName);
                });

        role.setName(roleName);
        return roleMapper.toDto(roleRepository.save(role));
    }

    @Override
    public void delete(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new NotFoundException("Role not found");
        }
        roleRepository.deleteById(id);
    }

    private String normalize(RoleRequestDto dto) {
        if (dto == null || dto.getName() == null) {
            throw new BadRequestException("Role name is required");
        }
        String name = dto.getName().trim().toUpperCase();
        if (name.isBlank()) {
            throw new BadRequestException("Role name is required");
        }
        return name.startsWith("ROLE_") ? name : "ROLE_" + name;
    }
}
