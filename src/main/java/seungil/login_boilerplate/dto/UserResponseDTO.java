package seungil.login_boilerplate.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private UUID id;
    private String userId;
    private String userName;

    public UserResponseDTO(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }
}
