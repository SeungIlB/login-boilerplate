package seungil.login_boilerplate.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import seungil.login_boilerplate.dto.UserRequestDTO;
import seungil.login_boilerplate.exception.UserAccountLockedException;
import seungil.login_boilerplate.exception.UserNotEnabledException;
import seungil.login_boilerplate.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        try {
            HttpHeaders headers = authService.login(userRequestDTO.getEmail(), userRequestDTO.getPassword());
            return ResponseEntity.ok()
                    .headers(headers)
                    .body("로그인에 성공했습니다.");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UserNotEnabledException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (UserAccountLockedException e) {
            return ResponseEntity.status(HttpStatus.LOCKED).body(e.getMessage());
        } catch (AuthenticationException e) {
            int remainingAttempts = authService.getRemainingLoginAttempts(userRequestDTO.getEmail());
            String message = "이메일 주소나 비밀번호가 올바르지 않습니다. " +
                    remainingAttempts + "번 더 로그인에 실패하면 계정이 잠길 수 있습니다.";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
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
