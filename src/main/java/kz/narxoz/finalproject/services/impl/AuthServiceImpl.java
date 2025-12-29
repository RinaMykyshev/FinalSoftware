package kz.narxoz.finalproject.services.impl;

import kz.narxoz.finalproject.dto.request.LoginRequestDto;
import kz.narxoz.finalproject.dto.request.RegisterRequestDto;
import kz.narxoz.finalproject.dto.response.UserResponseDto;
import kz.narxoz.finalproject.exception.BadRequestException;
import kz.narxoz.finalproject.exception.NotFoundException;
import kz.narxoz.finalproject.mappers.UserMapper;
import kz.narxoz.finalproject.models.Role;
import kz.narxoz.finalproject.models.User;
import kz.narxoz.finalproject.repositories.RoleRepository;
import kz.narxoz.finalproject.repositories.UserRepository;
import kz.narxoz.finalproject.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final String DEFAULT_USER_ROLE = "ROLE_USER";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(RegisterRequestDto dto) {
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
    public UserResponseDto login(LoginRequestDto dto) {
        if (dto == null || dto.getEmail() == null || dto.getEmail().isBlank()
                || dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new BadRequestException("Email and password are required");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        return userMapper.toDto(user);
    }
}
