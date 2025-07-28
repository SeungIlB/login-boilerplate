package seungil.login_boilerplate.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private UUID id;
    private String email;
    private String username;

    public UserResponseDTO(String email, String username) {
        this.email = email;
        this.username = username;
    }
}
