package kz.narxoz.finalproject.services;

import jakarta.validation.Valid;
import kz.narxoz.finalproject.dto.request.EnrollmentRequestDto;
import kz.narxoz.finalproject.dto.response.EnrollmentResponseDto;

import java.util.List;

public interface EnrollmentService {

    EnrollmentResponseDto enroll(EnrollmentRequestDto dto);

    EnrollmentResponseDto getById(Long id);

    List<EnrollmentResponseDto> getAll();

    EnrollmentResponseDto update(Long id, EnrollmentRequestDto dto);

    void delete(Long id);

    EnrollmentResponseDto create(@Valid EnrollmentRequestDto dto);
}
