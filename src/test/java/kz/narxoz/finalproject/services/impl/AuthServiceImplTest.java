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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserMapper userMapper;

    private AuthServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new AuthServiceImpl(userRepository, roleRepository, passwordEncoder, authenticationManager, userMapper);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void register_whenEmailTaken_throwsBadRequest() {
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setEmail("test@example.com");
        dto.setPassword("123456");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(new User()));

        assertThrows(BadRequestException.class, () -> service.register(dto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void register_whenRoleMissing_throwsNotFound() {
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setEmail("test@example.com");
        dto.setPassword("123456");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.register(dto));
    }

    @Test
    void register_success_savesUserWithEncodedPassword_andReturnsDto() {
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setEmail("test@example.com");
        dto.setPassword("123456");

        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");

        User saved = new User();
        saved.setId(10L);
        saved.setEmail("test@example.com");
        saved.setPassword("ENC");

        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(10L);
        responseDto.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("123456")).thenReturn("ENC");
        when(userRepository.save(any(User.class))).thenReturn(saved);
        when(userMapper.toDto(saved)).thenReturn(responseDto);

        UserResponseDto result = service.register(dto);

        assertEquals(10L, result.getId());
        assertEquals("test@example.com", result.getEmail());

        verify(userRepository).save(argThat(u ->
                u.getEmail().equals("test@example.com") &&
                        u.getPassword().equals("ENC") &&
                        !u.isBlocked() &&
                        u.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_USER"))
        ));
    }

    @Test
    void login_authenticatesSetsSecurityContext_andReturnsDto() {
        LoginRequestDto dto = new LoginRequestDto();
        dto.setEmail("test@example.com");
        dto.setPassword("123456");

        Authentication auth = new UsernamePasswordAuthenticationToken("test@example.com", "123456");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);

        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(1L);
        responseDto.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(responseDto);

        UserResponseDto result = service.login(dto);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("test@example.com", SecurityContextHolder.getContext().getAuthentication().getName());
        assertEquals("test@example.com", result.getEmail());
    }
}
