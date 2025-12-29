package kz.narxoz.finalproject.services.impl;

import kz.narxoz.finalproject.dto.request.ChangePasswordRequestDto;
import kz.narxoz.finalproject.dto.request.UpdateProfileRequestDto;
import kz.narxoz.finalproject.exception.BadRequestException;
import kz.narxoz.finalproject.models.User;
import kz.narxoz.finalproject.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new UserServiceImpl(userRepository, passwordEncoder);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("u@example.com", "pwd"));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void updateProfile_whenEmailInUse_throwsBadRequest() {
        User current = new User();
        current.setId(1L);
        current.setEmail("u@example.com");

        when(userRepository.findByEmail("u@example.com")).thenReturn(Optional.of(current));
        when(userRepository.findByEmail("taken@example.com")).thenReturn(Optional.of(new User()));

        UpdateProfileRequestDto dto = new UpdateProfileRequestDto();
        dto.setEmail("taken@example.com");

        assertThrows(BadRequestException.class, () -> service.updateProfile(dto));
    }

    @Test
    void updateProfile_success_updatesAndSaves() {
        User current = new User();
        current.setId(1L);
        current.setEmail("u@example.com");

        when(userRepository.findByEmail("u@example.com")).thenReturn(Optional.of(current));
        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());

        UpdateProfileRequestDto dto = new UpdateProfileRequestDto();
        dto.setEmail("new@example.com");

        service.updateProfile(dto);

        verify(userRepository).save(argThat(u -> u.getEmail().equals("new@example.com")));
    }

    @Test
    void changePassword_whenOldWrong_throwsBadRequest() {
        User current = new User();
        current.setEmail("u@example.com");
        current.setPassword("ENC_OLD");

        when(userRepository.findByEmail("u@example.com")).thenReturn(Optional.of(current));
        when(passwordEncoder.matches("old", "ENC_OLD")).thenReturn(false);

        ChangePasswordRequestDto dto = new ChangePasswordRequestDto();
        dto.setOldPassword("old");
        dto.setNewPassword("new");

        assertThrows(BadRequestException.class, () -> service.changePassword(dto));
    }

    @Test
    void changePassword_success_encodesAndSaves() {
        User current = new User();
        current.setEmail("u@example.com");
        current.setPassword("ENC_OLD");

        when(userRepository.findByEmail("u@example.com")).thenReturn(Optional.of(current));
        when(passwordEncoder.matches("old", "ENC_OLD")).thenReturn(true);
        when(passwordEncoder.encode("new")).thenReturn("ENC_NEW");

        ChangePasswordRequestDto dto = new ChangePasswordRequestDto();
        dto.setOldPassword("old");
        dto.setNewPassword("new");

        service.changePassword(dto);

        verify(userRepository).save(argThat(u -> u.getPassword().equals("ENC_NEW")));
    }
}
