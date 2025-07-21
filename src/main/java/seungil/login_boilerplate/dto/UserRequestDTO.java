package seungil.login_boilerplate.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import jakarta.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
public class UserRequestDTO {

    @NotBlank(message = "아이디를 입력해주세요")
    private final String userId;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$",
            message = "비밀번호는 영문 대소문자, 숫자, 특수문자를 포함하여 8~16자여야 합니다")
    private final String password;

    @NotBlank(message = "사용자 이름을 입력해주세요")
    private final String username;

    @Override
    public String toString() {
        return "SignUpRequest{" +
                "username='" + username + '\'' +
                ", password='[PROTECTED]'" +
                ", userId='" + userId + '\'' +
                '}';
    }
}
