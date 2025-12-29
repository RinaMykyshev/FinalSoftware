package kz.narxoz.finalproject.services.impl;

import kz.narxoz.finalproject.dto.request.EnrollmentRequestDto;
import kz.narxoz.finalproject.dto.response.EnrollmentResponseDto;
import kz.narxoz.finalproject.exception.BadRequestException;
import kz.narxoz.finalproject.exception.NotFoundException;
import kz.narxoz.finalproject.mappers.EnrollmentMapper;
import kz.narxoz.finalproject.models.Course;
import kz.narxoz.finalproject.models.Enrollment;
import kz.narxoz.finalproject.models.Student;
import kz.narxoz.finalproject.repositories.CourseRepository;
import kz.narxoz.finalproject.repositories.EnrollmentRepository;
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
class EnrollmentServiceImplTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private EnrollmentMapper enrollmentMapper;

    private EnrollmentServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new EnrollmentServiceImpl(enrollmentRepository, studentRepository, courseRepository, enrollmentMapper);
    }

    @Test
    void enroll_whenStudentMissing_throwsNotFound() {
        EnrollmentRequestDto req = new EnrollmentRequestDto();
        req.setStudentId(1L);
        req.setCourseId(2L);

        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.enroll(req));
    }

    @Test
    void enroll_whenCourseMissing_throwsNotFound() {
        EnrollmentRequestDto req = new EnrollmentRequestDto();
        req.setStudentId(1L);
        req.setCourseId(2L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(new Student()));
        when(courseRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.enroll(req));
    }

    @Test
    void enroll_whenAlreadyEnrolled_throwsBadRequest() {
        EnrollmentRequestDto req = new EnrollmentRequestDto();
        req.setStudentId(1L);
        req.setCourseId(2L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(new Student()));
        when(courseRepository.findById(2L)).thenReturn(Optional.of(new Course()));
        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 2L)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> service.enroll(req));
    }

    @Test
    void enroll_success_savesAndReturnsDto() {
        EnrollmentRequestDto req = new EnrollmentRequestDto();
        req.setStudentId(1L);
        req.setCourseId(2L);

        Student student = new Student();
        student.setId(1L);
        Course course = new Course();
        course.setId(2L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(2L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 2L)).thenReturn(false);

        Enrollment saved = new Enrollment();
        saved.setId(100L);
        saved.setStudent(student);
        saved.setCourse(course);

        EnrollmentResponseDto dto = new EnrollmentResponseDto();
        dto.setId(100L);

        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(saved);
        when(enrollmentMapper.toDto(saved)).thenReturn(dto);

        EnrollmentResponseDto result = service.enroll(req);

        assertEquals(100L, result.getId());
        verify(enrollmentRepository).save(any(Enrollment.class));
    }

    @Test
    void getById_whenMissing_throwsNotFound() {
        when(enrollmentRepository.findById(77L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getById(77L));
    }

    @Test
    void getAll_returnsMappedList() {
        Enrollment e1 = new Enrollment();
        Enrollment e2 = new Enrollment();

        when(enrollmentRepository.findAll()).thenReturn(List.of(e1, e2));
        when(enrollmentMapper.toDto(e1)).thenReturn(new EnrollmentResponseDto());
        when(enrollmentMapper.toDto(e2)).thenReturn(new EnrollmentResponseDto());

        List<EnrollmentResponseDto> result = service.getAll();

        assertEquals(2, result.size());
        verify(enrollmentRepository).findAll();
        verify(enrollmentMapper).toDto(e1);
        verify(enrollmentMapper).toDto(e2);
    }

    @Test
    void delete_whenMissing_throwsNotFound() {
        when(enrollmentRepository.existsById(5L)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> service.delete(5L));
    }

    @Test
    void update_whenEnrollmentMissing_throwsNotFound() {
        EnrollmentRequestDto req = new EnrollmentRequestDto();
        req.setStudentId(1L);
        req.setCourseId(2L);

        when(enrollmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.update(1L, req));
    }
}
