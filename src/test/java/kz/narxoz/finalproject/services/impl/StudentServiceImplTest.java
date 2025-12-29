package kz.narxoz.finalproject.services.impl;

import kz.narxoz.finalproject.dto.request.StudentRequestDto;
import kz.narxoz.finalproject.dto.response.StudentResponseDto;
import kz.narxoz.finalproject.exception.NotFoundException;
import kz.narxoz.finalproject.mappers.StudentMapper;
import kz.narxoz.finalproject.models.Student;
import kz.narxoz.finalproject.repositories.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentMapper studentMapper;

    private StudentServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new StudentServiceImpl(studentRepository, studentMapper);
    }

    @Test
    void create_savesAndReturnsDto() {
        StudentRequestDto req = new StudentRequestDto();
        req.setName("Renat Mykysh");

        Student saved = new Student();
        saved.setId(1L);
        saved.setName("Renat Mykysh");

        StudentResponseDto dto = new StudentResponseDto();
        dto.setId(1L);
        dto.setName("Renat Mykysh");

        when(studentRepository.save(any(Student.class))).thenReturn(saved);
        when(studentMapper.toDto(saved)).thenReturn(dto);

        StudentResponseDto result = service.create(req);

        assertEquals(1L, result.getId());
        verify(studentRepository).save(any(Student.class));
        verify(studentMapper).toDto(saved);
    }

    @Test
    void getById_whenMissing_throwsNotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getById(99L));
    }

    @Test
    void getAll_mapsAll() {
        when(studentRepository.findAll()).thenReturn(List.of(new Student(), new Student()));
        when(studentMapper.toDto(any(Student.class))).thenReturn(new StudentResponseDto());

        List<StudentResponseDto> list = service.getAll();

        assertEquals(2, list.size());
        verify(studentRepository).findAll();
    }

    @Test
    void update_whenExists_updatesAndReturnsDto() {
        Student existing = new Student();
        existing.setId(5L);
        existing.setName("Old");

        StudentRequestDto req = new StudentRequestDto();
        req.setName("New");

        Student saved = new Student();
        saved.setId(5L);
        saved.setName("New");

        StudentResponseDto dto = new StudentResponseDto();
        dto.setId(5L);
        dto.setName("New");

        when(studentRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(studentRepository.save(existing)).thenReturn(saved);
        when(studentMapper.toDto(saved)).thenReturn(dto);

        StudentResponseDto result = service.update(5L, req);

        assertEquals("New", result.getName());
        verify(studentRepository).save(existing);
    }

    @Test
    void delete_whenMissing_throwsNotFound() {
        when(studentRepository.existsById(10L)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> service.delete(10L));
    }

    @Test
    void delete_whenExists_deletes() {
        when(studentRepository.existsById(10L)).thenReturn(true);
        service.delete(10L);
        verify(studentRepository).deleteById(10L);
    }
}