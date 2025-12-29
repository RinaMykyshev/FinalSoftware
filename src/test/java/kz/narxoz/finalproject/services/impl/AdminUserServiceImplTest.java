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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminUserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    private AdminUserServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new AdminUserServiceImpl(userRepository, roleRepository, passwordEncoder, userMapper);
    }

    @Test
    void createUser_whenEmailTaken_throwsBadRequest() {
        AdminCreateUserRequestDto dto = new AdminCreateUserRequestDto();
        dto.setEmail("a@b.com");
        dto.setPassword("123456");

        when(userRepository.findByEmail("a@b.com")).thenReturn(Optional.of(new User()));

        assertThrows(BadRequestException.class, () -> service.createUser(dto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_success_savesAndReturnsDto() {
        AdminCreateUserRequestDto dto = new AdminCreateUserRequestDto();
        dto.setEmail("a@b.com");
        dto.setPassword("123456");

        Role role = new Role();
        role.setName("ROLE_USER");

        User saved = new User();
        saved.setId(5L);
        saved.setEmail("a@b.com");

        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(5L);
        responseDto.setEmail("a@b.com");

        when(userRepository.findByEmail("a@b.com")).thenReturn(Optional.empty());
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("123456")).thenReturn("ENC");
        when(userRepository.save(any(User.class))).thenReturn(saved);
        when(userMapper.toDto(saved)).thenReturn(responseDto);

        UserResponseDto result = service.createUser(dto);

        assertEquals(5L, result.getId());
        assertEquals("a@b.com", result.getEmail());
        verify(userRepository).save(argThat(u -> u.getEmail().equals("a@b.com") && u.getPassword().equals("ENC")));
    }

    @Test
    void blockUser_whenMissing_throwsNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.blockUser(1L));
    }

    @Test
    void deleteUser_whenMissing_throwsNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> service.deleteUser(1L));
    }

    @Test
    void updateUserRoles_whenRoleMissing_throwsNotFound() {
        User user = new User();
        user.setId(1L);
        user.setEmail("u@x.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findByName("ROLE_MANAGER")).thenReturn(Optional.empty());

        UpdateUserRolesRequestDto dto = new UpdateUserRolesRequestDto();
        dto.setRoles(Set.of("MANAGER"));

        assertThrows(NotFoundException.class, () -> service.updateUserRoles(1L, dto));
    }
}
