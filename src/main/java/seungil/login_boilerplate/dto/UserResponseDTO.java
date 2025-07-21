package seungil.login_boilerplate.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class UserResponseDTO {

    private UUID id;
    private String userId;
    private String userName;
}
