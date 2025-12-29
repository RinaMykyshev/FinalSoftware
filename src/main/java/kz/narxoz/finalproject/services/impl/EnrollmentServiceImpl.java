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
import kz.narxoz.finalproject.services.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentMapper enrollmentMapper;

    @Override
    public EnrollmentResponseDto enroll(EnrollmentRequestDto dto) {
        validate(dto);

        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new NotFoundException("Student not found"));

        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));

        boolean exists = enrollmentRepository.existsByStudentIdAndCourseId(dto.getStudentId(), dto.getCourseId());
        if (exists) {
            throw new BadRequestException("Student already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);

        return enrollmentMapper.toDto(enrollmentRepository.save(enrollment));
    }

    @Override
    public EnrollmentResponseDto getById(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Enrollment not found"));
        return enrollmentMapper.toDto(enrollment);
    }

    @Override
    public List<EnrollmentResponseDto> getAll() {
        return enrollmentRepository.findAll().stream()
                .map(enrollmentMapper::toDto)
                .toList();
    }

    @Override
    public EnrollmentResponseDto update(Long id, EnrollmentRequestDto dto) {
        validate(dto);

        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Enrollment not found"));

        boolean samePair = enrollment.getStudent() != null && enrollment.getCourse() != null
                && enrollment.getStudent().getId().equals(dto.getStudentId())
                && enrollment.getCourse().getId().equals(dto.getCourseId());

        if (!samePair && enrollmentRepository.existsByStudentIdAndCourseId(dto.getStudentId(), dto.getCourseId())) {
            throw new BadRequestException("Student already enrolled in this course");
        }

        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new NotFoundException("Student not found"));

        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));

        enrollment.setStudent(student);
        enrollment.setCourse(course);

        return enrollmentMapper.toDto(enrollmentRepository.save(enrollment));
    }

    @Override
    public void delete(Long id) {
        if (!enrollmentRepository.existsById(id)) {
            throw new NotFoundException("Enrollment not found");
        }
        enrollmentRepository.deleteById(id);
    }

    private void validate(EnrollmentRequestDto dto) {
        if (dto == null || dto.getStudentId() == null || dto.getCourseId() == null) {
            throw new BadRequestException("studentId and courseId are required");
        }
    }
}
