package kz.narxoz.finalproject.mappers;

import kz.narxoz.finalproject.dto.response.StudentResponseDto;
import kz.narxoz.finalproject.models.Student;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    StudentResponseDto toDto(Student student);
}
