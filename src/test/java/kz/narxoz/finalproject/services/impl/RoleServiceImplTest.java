package kz.narxoz.finalproject.services.impl;

import kz.narxoz.finalproject.dto.request.RoleRequestDto;
import kz.narxoz.finalproject.dto.response.RoleResponseDto;
import kz.narxoz.finalproject.exception.BadRequestException;
import kz.narxoz.finalproject.exception.NotFoundException;
import kz.narxoz.finalproject.mappers.RoleMapper;
import kz.narxoz.finalproject.models.Role;
import kz.narxoz.finalproject.repositories.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    private RoleServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new RoleServiceImpl(roleRepository, roleMapper);
    }

    @Test
    void create_whenAlreadyExists_throwsBadRequest() {
        RoleRequestDto dto = new RoleRequestDto();
        dto.setName("MANAGER");

        when(roleRepository.findByName("ROLE_MANAGER")).thenReturn(Optional.of(new Role()));

        assertThrows(BadRequestException.class, () -> service.create(dto));
        verify(roleRepository, never()).save(any(Role.class));
    }

    @Test
    void create_success_savesAndReturnsDto() {
        RoleRequestDto dto = new RoleRequestDto();
        dto.setName("MANAGER");

        when(roleRepository.findByName("ROLE_MANAGER")).thenReturn(Optional.empty());

        Role saved = new Role();
        saved.setId(10L);
        saved.setName("ROLE_MANAGER");

        RoleResponseDto resp = new RoleResponseDto();
        resp.setId(10L);
        resp.setName("ROLE_MANAGER");

        when(roleRepository.save(any(Role.class))).thenReturn(saved);
        when(roleMapper.toDto(saved)).thenReturn(resp);

        RoleResponseDto result = service.create(dto);

        assertEquals(10L, result.getId());
        assertEquals("ROLE_MANAGER", result.getName());
        verify(roleRepository).save(argThat(r -> "ROLE_MANAGER".equals(r.getName())));
    }

    @Test
    void getById_whenMissing_throwsNotFound() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getById(1L));
    }
}
