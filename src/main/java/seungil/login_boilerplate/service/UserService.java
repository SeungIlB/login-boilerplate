package seungil.login_boilerplate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seungil.login_boilerplate.domain.User;
import seungil.login_boilerplate.dto.UpdateUserDTO;
import seungil.login_boilerplate.dto.UserRequestDTO;
import seungil.login_boilerplate.dto.UserResponseDTO;
import seungil.login_boilerplate.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

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

    // 회원 정보 조회
    public UserResponseDTO getUserById(UUID userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return new UserResponseDTO(user.get().getId(), user.get().getUserId(), user.get().getUserName());
        } else {
            throw new IllegalStateException("사용자를 찾을 수 없습니다.");
        }
    }

    // 회원 정보 수정
    public UserResponseDTO updateUser(UUID id, UserRequestDTO dto) {
        return userRepository.findById(id).map(existingUser -> {
            if (!existingUser.getUserId().equals(dto.getUserId()) && isUserIdAlreadyExists(dto.getUserId())) {
                throw new IllegalStateException("이미 등록된 userId입니다.");
            }

            String encodedPassword;
            if (dto.getPassword() == null || dto.getPassword().isBlank()) {
                // 변경 안 하는 경우 기존 비밀번호 유지
                encodedPassword = existingUser.getPassword();
            } else {
                // 변경 시 암호화해서 넣기
                encodedPassword = passwordEncoder.encode(dto.getPassword());
            }

            User updatedUser = User.builder()
                    .id(existingUser.getId()) // ID 유지
                    .userId(dto.getUserId())
                    .password(encodedPassword)
                    .userName(dto.getUsername())
                    .created_at(existingUser.getCreated_at()) // 생성일 유지
                    .updated_at(LocalDateTime.now()) // 수동 갱신
                    .accountNonExpired(existingUser.isAccountNonExpired())
                    .accountNonLocked(existingUser.isAccountNonLocked())
                    .credentialsNonExpired(existingUser.isCredentialsNonExpired())
                    .enabled(existingUser.isEnabled())
                    .build();

            userRepository.save(updatedUser);
            return new UserResponseDTO(updatedUser.getId(), updatedUser.getUserId(), updatedUser.getUserName());
        }).orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));
    }

    // 회원 삭제
    public void deleteUser(UUID userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new IllegalStateException("사용자를 찾을 수 없습니다.");
        }
    }
}