package kz.narxoz.finalproject.services.impl;

import kz.narxoz.finalproject.dto.request.AdminCreateUserRequestDto;
import kz.narxoz.finalproject.dto.request.UpdateUserRolesRequestDto;
import kz.narxoz.finalproject.dto.response.UserResponseDto;
import kz.narxoz.finalproject.exception.BadRequestException;
import kz.narxoz.finalproject.exception.NotFoundException;
import kz.narxoz.finalproject.mappers.UserMapper;
import kz.narxoz.finalproject.models.Role;
import kz.narxoz.finalproject.models.User;
import kz.narxoz.finalproject.repositories.RoleRepository;
import kz.narxoz.finalproject.repositories.UserRepository;
import kz.narxoz.finalproject.services.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private static final String DEFAULT_USER_ROLE = "ROLE_USER";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto createUser(AdminCreateUserRequestDto dto) {
        if (dto == null || dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new BadRequestException("Email is required");
        }
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new BadRequestException("Password is required");
        }
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new BadRequestException("Email already in use");
        }

        Role userRole = roleRepository.findByName(DEFAULT_USER_ROLE)
                .orElseThrow(() -> new NotFoundException("Default role not found: " + DEFAULT_USER_ROLE));

        User user = new User();
        user.setEmail(dto.getEmail().trim());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setBlocked(false);
        user.setRoles(Set.of(userRole));

        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    @Override
    public UserResponseDto blockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        user.setBlocked(true);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto unblockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        user.setBlocked(false);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto updateUserRoles(Long userId, UpdateUserRolesRequestDto dto) {
        if (dto == null || dto.getRoles() == null || dto.getRoles().isEmpty()) {
            throw new BadRequestException("roles must not be empty");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Set<Role> roles = dto.getRoles().stream()
                .map(this::normalizeRoleName)
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new NotFoundException("Role not found: " + roleName)))
                .collect(Collectors.toSet());

        user.setRoles(roles);

        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    @Override
    public UserResponseDto getById(Long userId) {
        return userMapper.toDto(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found")));
    }

    @Override
    public List<UserResponseDto> getAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found");
        }
        userRepository.deleteById(userId);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return List.of();
    }

    private String normalizeRoleName(String roleName) {
        if (roleName == null) {
            throw new BadRequestException("Role name must not be null");
        }
        String normalized = roleName.trim().toUpperCase();
        if (normalized.isBlank()) {
            throw new BadRequestException("Role name must not be blank");
        }
        return normalized.startsWith("ROLE_") ? normalized : "ROLE_" + normalized;
    }
}
