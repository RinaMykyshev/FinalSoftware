package kz.narxoz.finalproject.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UpdateUserRolesRequestDto {

    @NotEmpty(message = "roles must not be empty")
    private Set<@NotBlank(message = "role must not be blank") String> roles;
}
