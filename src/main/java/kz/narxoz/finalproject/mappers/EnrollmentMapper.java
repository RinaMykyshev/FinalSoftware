package kz.narxoz.finalproject.mappers;

import kz.narxoz.finalproject.dto.response.EnrollmentResponseDto;
import kz.narxoz.finalproject.models.Enrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {

    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "student.name", target = "studentName")
    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "course.name", target = "courseName")
    EnrollmentResponseDto toDto(Enrollment enrollment);
}
