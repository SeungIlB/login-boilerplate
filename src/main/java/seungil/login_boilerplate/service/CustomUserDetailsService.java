package seungil.login_boilerplate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import seungil.login_boilerplate.domain.CustomUserDetails;
import seungil.login_boilerplate.domain.User;
import seungil.login_boilerplate.repository.UserRepository;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override // userId을 기준으로 사용자를 로드하는 메서드
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        // userId로 사용자 조회
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("해당 userId를 가진 사용자를 찾을 수 없습니다: " + userId));

        // 조회된 사용자 정보를 기반으로 CustomUserDetails 객체 생성 후 반환
        return new CustomUserDetails(
                user.getId(),         // UUID 추가
                user.getUserId(),
                user.getPassword(),
                user.getUserName(),
                user.isAccountNonExpired(),
                user.isAccountNonLocked(),
                user.isCredentialsNonExpired(),
                user.isEnabled(),
                Collections.emptyList()
        );
    }
}
