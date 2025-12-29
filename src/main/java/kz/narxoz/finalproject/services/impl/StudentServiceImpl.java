package kz.narxoz.finalproject.services.impl;

import kz.narxoz.finalproject.exception.NotFoundException;
import kz.narxoz.finalproject.dto.request.StudentRequestDto;
import kz.narxoz.finalproject.dto.response.StudentResponseDto;
import kz.narxoz.finalproject.mappers.StudentMapper;
import kz.narxoz.finalproject.models.Student;
import kz.narxoz.finalproject.repositories.StudentRepository;
import kz.narxoz.finalproject.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Override
    public StudentResponseDto create(StudentRequestDto dto) {
        Student student = new Student();
        student.setName(dto.getName());
        return studentMapper.toDto(studentRepository.save(student));
    }

    @Override
    public StudentResponseDto getById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found"));
        return studentMapper.toDto(student);
    }

    @Override
    public List<StudentResponseDto> getAll() {
        return studentRepository.findAll().stream()
                .map(studentMapper::toDto)
                .toList();
    }

    @Override
    public StudentResponseDto update(Long id, StudentRequestDto dto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found"));
        student.setName(dto.getName());
        return studentMapper.toDto(studentRepository.save(student));
    }

    @Override
    public void delete(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new NotFoundException("Student not found");
        }
        studentRepository.deleteById(id);
    }
}
