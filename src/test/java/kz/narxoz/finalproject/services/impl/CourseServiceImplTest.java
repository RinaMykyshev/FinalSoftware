package kz.narxoz.finalproject.services.impl;

import kz.narxoz.finalproject.dto.request.CourseRequestDto;
import kz.narxoz.finalproject.dto.response.CourseResponseDto;
import kz.narxoz.finalproject.exception.NotFoundException;
import kz.narxoz.finalproject.mappers.CourseMapper;
import kz.narxoz.finalproject.models.Course;
import kz.narxoz.finalproject.repositories.CourseRepository;
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
class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseMapper courseMapper;

    private CourseServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CourseServiceImpl(courseRepository, courseMapper);
    }

    @Test
    void create_savesAndReturnsDto() {
        CourseRequestDto req = new CourseRequestDto();
        req.setName("Algorithms");

        Course saved = new Course();
        saved.setId(1L);
        saved.setName("Algorithms");

        CourseResponseDto resp = new CourseResponseDto();
        resp.setId(1L);
        resp.setName("Algorithms");

        when(courseRepository.save(any(Course.class))).thenReturn(saved);
        when(courseMapper.toDto(saved)).thenReturn(resp);

        CourseResponseDto result = service.create(req);

        assertEquals(1L, result.getId());
        assertEquals("Algorithms", result.getName());
        verify(courseRepository).save(any(Course.class));
        verify(courseMapper).toDto(saved);
    }

    @Test
    void getById_whenExists_returnsDto() {
        Course course = new Course();
        course.setId(10L);
        course.setName("Databases");

        CourseResponseDto dto = new CourseResponseDto();
        dto.setId(10L);
        dto.setName("Databases");

        when(courseRepository.findById(10L)).thenReturn(Optional.of(course));
        when(courseMapper.toDto(course)).thenReturn(dto);

        CourseResponseDto result = service.getById(10L);

        assertEquals(10L, result.getId());
        verify(courseRepository).findById(10L);
    }

    @Test
    void getById_whenMissing_throwsNotFound() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getById(99L));
    }

    @Test
    void getAll_mapsAll() {
        Course c1 = new Course();
        c1.setId(1L);
        Course c2 = new Course();
        c2.setId(2L);

        when(courseRepository.findAll()).thenReturn(List.of(c1, c2));
        when(courseMapper.toDto(c1)).thenReturn(new CourseResponseDto());
        when(courseMapper.toDto(c2)).thenReturn(new CourseResponseDto());

        List<CourseResponseDto> result = service.getAll();

        assertEquals(2, result.size());
        verify(courseRepository).findAll();
        verify(courseMapper).toDto(c1);
        verify(courseMapper).toDto(c2);
    }

    @Test
    void update_whenExists_updatesAndReturnsDto() {
        Course existing = new Course();
        existing.setId(5L);
        existing.setName("Old");

        CourseRequestDto req = new CourseRequestDto();
        req.setName("New");

        Course saved = new Course();
        saved.setId(5L);
        saved.setName("New");

        CourseResponseDto dto = new CourseResponseDto();
        dto.setId(5L);
        dto.setName("New");

        when(courseRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(courseRepository.findByNameIgnoreCase("New")).thenReturn(Optional.empty());
        when(courseRepository.save(existing)).thenReturn(saved);
        when(courseMapper.toDto(saved)).thenReturn(dto);

        CourseResponseDto result = service.update(5L, req);

        assertEquals("New", result.getName());
        verify(courseRepository).save(existing);
    }

    @Test
    void delete_whenMissing_throwsNotFound() {
        when(courseRepository.existsById(7L)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> service.delete(7L));
    }

    @Test
    void delete_whenExists_deletes() {
        when(courseRepository.existsById(7L)).thenReturn(true);
        service.delete(7L);
        verify(courseRepository).deleteById(7L);
    }
}
