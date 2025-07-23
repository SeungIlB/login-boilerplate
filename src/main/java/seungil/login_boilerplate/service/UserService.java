package seungil.login_boilerplate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seungil.login_boilerplate.domain.User;
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
    private final MailService mailService;


    public UserResponseDTO signUp(UserRequestDTO userRequestDTO) {
        // 이메일 중복 체크
        if (isEmailAlreadyExists(userRequestDTO.getEmail())) {
            throw new IllegalStateException("이미 등록된 이메일 입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = encodePassword(userRequestDTO.getPassword());

        String verificationToken = UUID.randomUUID().toString();

        // 회원 정보 생성
        User user = User.builder()
                .email(userRequestDTO.getEmail())
                .userName(userRequestDTO.getUsername())
                .password(encodedPassword)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .mailVerificationToken(verificationToken)
                .build();

        sendEmail(user.getEmail(), verificationToken, "회원가입 이메일 인증");
        // 회원 저장
        userRepository.save(user);

        // UserResponseDTO 생성
        return new UserResponseDTO(user.getId(), user.getUserName(), user.getEmail());
    }

    // 이메일 중복 체크 메서드
    public boolean isEmailAlreadyExists(String email){
        return userRepository.existsByEmail(email);
    }

    // 이메일 전송 메서드
    private void sendEmail(String email, String verificationToken, String subject) {
        String verificationUrl = "http://localhost:9090/user/verify/" + verificationToken;
        mailService.sendEmail(email, verificationUrl, subject);
    }

    // 이메일 토큰을 사용하여 사용자 조회
    private User findUserByVerificationToken(String token) {
        return userRepository.findByMailVerificationToken(token)
                .orElseThrow(() -> new IllegalStateException("유효한 토큰이 없습니다."));
    }

    // 이메일 검증
    public UserResponseDTO verifyEmail(String token) {
        User user = findUserByVerificationToken(token);
        user.enableAccount(); // 엔티티 메서드 사용
        userRepository.save(user);
        return new UserResponseDTO(user.getEmail(), user.getUserName());
    }

    // 비밀번호 암호화 메서드
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    // 회원 정보 조회
    public UserResponseDTO getUserById(UUID userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return new UserResponseDTO(user.get().getId(), user.get().getEmail(), user.get().getUserName());
        } else {
            throw new IllegalStateException("사용자를 찾을 수 없습니다.");
        }
    }

    // 회원 정보 수정
    public UserResponseDTO updateUser(UUID userId, UserRequestDTO userRequestDTO) {
        return userRepository.findById(userId).map(user -> {
            if (!user.getEmail().equals(userRequestDTO.getEmail())) {
                verifyEmail(userRequestDTO.getEmail());
            }
            String verificationToken = UUID.randomUUID().toString();
            user.updateUser(userRequestDTO, passwordEncoder,verificationToken);
            sendEmail(user.getEmail(), verificationToken, "회원 정보 수정용 이메일 인증");

            userRepository.save(user);
            return new UserResponseDTO(user.getEmail(), user.getUserName());
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