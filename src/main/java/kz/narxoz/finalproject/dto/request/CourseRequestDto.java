package kz.narxoz.finalproject.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseRequestDto {

    @NotBlank(message = "name is required")
    @Size(min = 2, max = 255, message = "name length must be between 2 and 255")
    private String name;
}
