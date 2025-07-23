package seungil.login_boilerplate.service;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import seungil.login_boilerplate.domain.CustomUserDetails;
import seungil.login_boilerplate.jwt.JwtTokenProvider;
import org.springframework.security.core.AuthenticationException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public HttpHeaders login(String email, String password) {
        try {
            // 인증 수행
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));

            // 토큰 생성
            String accessToken = jwtTokenProvider.createToken(authentication);
            String refreshToken = jwtTokenProvider.createRefreshToken(authentication);
            String uuid = ((CustomUserDetails) authentication.getPrincipal()).getId().toString();

            // 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
            headers.add("Refresh-Token", refreshToken);
            headers.add("userUUID", uuid);

            return headers;
        } catch (AuthenticationException e) {
            // 인증 실패 시 예외 처리
            throw new AuthenticationException("Authentication failed: " + e.getMessage()) {};
        }
    }


    // Refresh 토큰을 블랙리스트에 추가하고, 성공적으로 추가되면 true를 반환한다.
    public boolean logout(String token) {
        return jwtTokenProvider.blacklistRefreshToken(token);
    }
}
