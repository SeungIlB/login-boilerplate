package seungil.login_boilerplate.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDTO {
    private String email;
    private String password;
    private String username;
}
