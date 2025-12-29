package kz.narxoz.finalproject.services.impl;

import kz.narxoz.finalproject.dto.request.CourseRequestDto;
import kz.narxoz.finalproject.dto.response.CourseResponseDto;
import kz.narxoz.finalproject.exception.BadRequestException;
import kz.narxoz.finalproject.exception.NotFoundException;
import kz.narxoz.finalproject.mappers.CourseMapper;
import kz.narxoz.finalproject.models.Course;
import kz.narxoz.finalproject.repositories.CourseRepository;
import kz.narxoz.finalproject.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    public CourseResponseDto create(CourseRequestDto dto) {
        String name = normalizeName(dto);

        if (courseRepository.existsByNameIgnoreCase(name)) {
            throw new BadRequestException("Course with this name already exists");
        }

        Course course = new Course();
        course.setName(name);

        return courseMapper.toDto(courseRepository.save(course));
    }

    @Override
    public CourseResponseDto getById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Course not found"));
        return courseMapper.toDto(course);
    }

    @Override
    public List<CourseResponseDto> getAll() {
        return courseRepository.findAll().stream()
                .map(courseMapper::toDto)
                .toList();
    }

    @Override
    public CourseResponseDto update(Long id, CourseRequestDto dto) {
        String name = normalizeName(dto);

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Course not found"));

        courseRepository.findByNameIgnoreCase(name)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new BadRequestException("Course with this name already exists");
                });

        course.setName(name);
        return courseMapper.toDto(courseRepository.save(course));
    }

    @Override
    public void delete(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new NotFoundException("Course not found");
        }
        courseRepository.deleteById(id);
    }

    private String normalizeName(CourseRequestDto dto) {
        if (dto == null || dto.getName() == null) {
            throw new BadRequestException("Course name is required");
        }
        String name = dto.getName().trim();
        if (name.isBlank()) {
            throw new BadRequestException("Course name is required");
        }
        return name;
    }
}
