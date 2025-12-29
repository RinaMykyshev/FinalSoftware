package kz.narxoz.finalproject.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnrollmentResponseDto {
    private Long id;

    private Long studentId;
    private String studentName;

    private Long courseId;
    private String courseName;
}
