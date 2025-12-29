package kz.narxoz.finalproject.mappers;

import kz.narxoz.finalproject.dto.response.CourseResponseDto;
import kz.narxoz.finalproject.models.Course;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    CourseResponseDto toDto(Course course);
}
