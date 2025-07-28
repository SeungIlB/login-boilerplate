package seungil.login_boilerplate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDTO {
    private int statusCode;
    private String message;
}

