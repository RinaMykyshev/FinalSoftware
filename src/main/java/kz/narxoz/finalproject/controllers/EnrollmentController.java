package kz.narxoz.finalproject.controllers;

import jakarta.validation.Valid;
import kz.narxoz.finalproject.dto.request.EnrollmentRequestDto;
import kz.narxoz.finalproject.dto.response.EnrollmentResponseDto;
import kz.narxoz.finalproject.services.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('USER','ADMIN','MANAGER')")
    public EnrollmentResponseDto enroll(@Valid @RequestBody EnrollmentRequestDto dto) {
        return enrollmentService.enroll(dto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public EnrollmentResponseDto getById(@PathVariable Long id) {
        return enrollmentService.getById(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public List<EnrollmentResponseDto> getAll() {
        return enrollmentService.getAll();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public EnrollmentResponseDto update(@PathVariable Long id, @Valid @RequestBody EnrollmentRequestDto dto) {
        return enrollmentService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public void delete(@PathVariable Long id) {
        enrollmentService.delete(id);
    }
}
