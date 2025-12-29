package kz.narxoz.finalproject.controllers.rest;

import jakarta.validation.Valid;
import kz.narxoz.finalproject.dto.request.EnrollmentRequestDto;
import kz.narxoz.finalproject.dto.response.EnrollmentResponseDto;
import kz.narxoz.finalproject.services.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentRestController {

    private final EnrollmentService enrollmentService;

    @GetMapping
    public ResponseEntity<List<EnrollmentResponseDto>> getAll() {
        return ResponseEntity.ok(enrollmentService.getAll());
    }

    @PostMapping
    public ResponseEntity<EnrollmentResponseDto> create(@Valid @RequestBody EnrollmentRequestDto dto) {
        return ResponseEntity.ok(enrollmentService.create(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        enrollmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
