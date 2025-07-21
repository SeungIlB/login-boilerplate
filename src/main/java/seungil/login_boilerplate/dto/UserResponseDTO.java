package seungil.login_boilerplate.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class UserResponseDTO {

    private UUID id;
    private String userId;
    private String userName;
}
