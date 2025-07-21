package seungil.login_boilerplate.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDTO {
    private String userId;
    private String password;
    private String userName;
}
