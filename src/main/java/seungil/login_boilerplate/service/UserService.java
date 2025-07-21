package seungil.login_boilerplate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seungil.login_boilerplate.domain.User;
import seungil.login_boilerplate.dto.UserRequestDTO;
import seungil.login_boilerplate.dto.UserResponseDTO;
import seungil.login_boilerplate.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO signUp(UserRequestDTO userRequestDTO) {
        // userId 중복 체크
        if (isUserIdAlreadyExists(userRequestDTO.getUserId())) {
            throw new IllegalStateException("이미 등록된 사용자Id 입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = encodePassword(userRequestDTO.getPassword());

        // 회원 정보 생성
        User user = User.builder()
                .userId(userRequestDTO.getUserId())
                .userName(userRequestDTO.getUsername())
                .password(encodedPassword)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();

        // 회원 저장
        userRepository.save(user);

        // UserResponseDTO 생성
        return new UserResponseDTO(user.getId(), user.getUserName(), user.getUserId());
    }

    // userId 중복 체크 메서드
    public boolean isUserIdAlreadyExists(String userId) {
        return userRepository.existsByUserId(userId);
    }

    // 비밀번호 암호화 메서드
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}