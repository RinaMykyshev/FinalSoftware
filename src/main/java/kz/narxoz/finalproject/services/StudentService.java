package kz.narxoz.finalproject.services;

import kz.narxoz.finalproject.dto.request.StudentRequestDto;
import kz.narxoz.finalproject.dto.response.StudentResponseDto;

import java.util.List;

public interface StudentService {
    StudentResponseDto create(StudentRequestDto dto);
    StudentResponseDto getById(Long id);
    List<StudentResponseDto> getAll();
    StudentResponseDto update(Long id, StudentRequestDto dto);
    void delete(Long id);
}
