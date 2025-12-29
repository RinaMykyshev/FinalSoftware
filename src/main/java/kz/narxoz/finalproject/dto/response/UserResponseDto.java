package kz.narxoz.finalproject.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserResponseDto {
    private Long id;
    private String email;
    private boolean blocked;
    private Set<String> roles;
}
