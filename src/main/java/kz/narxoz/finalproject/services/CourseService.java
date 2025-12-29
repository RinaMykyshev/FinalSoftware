package kz.narxoz.finalproject.services;

import kz.narxoz.finalproject.dto.request.CourseRequestDto;
import kz.narxoz.finalproject.dto.response.CourseResponseDto;

import java.util.List;

public interface CourseService {
    CourseResponseDto create(CourseRequestDto dto);
    CourseResponseDto getById(Long id);
    List<CourseResponseDto> getAll();
    CourseResponseDto update(Long id, CourseRequestDto dto);
    void delete(Long id);
}
