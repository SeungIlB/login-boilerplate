package seungil.login_boilerplate.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import seungil.login_boilerplate.dto.UserRequestDTO;
import seungil.login_boilerplate.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody UserRequestDTO userRequestDTO) {
        try {
            HttpHeaders headers = authService.login(userRequestDTO.getEmail(), userRequestDTO.getPassword());

            Response response = new Response(HttpStatus.OK.value(),  "로그인에 성공했습니다.");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(response);
        } catch (AuthenticationException e) {
            Response errorResponse = new Response(HttpStatus.UNAUTHORIZED.value(), "userId나 비밀번호가 올바르지 않습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(errorResponse);
        }
    }


    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(name = "Refresh-Token") String refreshToken) {
        boolean logoutSuccess = authService.logout(refreshToken);

        if (logoutSuccess) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // or INTERNAL_SERVER_ERROR
        }
    }


    @Getter
    public static class Response {
        private int statusCode;
        private String message;

        public Response(int statusCode, String message) {
            this.statusCode = statusCode;
            this.message = message;
        }
    }
}
