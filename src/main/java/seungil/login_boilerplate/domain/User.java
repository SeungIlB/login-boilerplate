package seungil.login_boilerplate.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user")
@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String userName;

    @CreationTimestamp // INSERT 쿼리가 발생할 때, 현재 시간을 자동으로 저장
    private LocalDateTime created_at; // 회원가입한 시간

    @UpdateTimestamp // UPDATE 쿼리가 발생할 때, 현재 시간을 자동으로 저장
    private LocalDateTime updated_at; // 마지막으로 수정한 시간

    private boolean accountNonExpired; // 계정 만료 여부
    private boolean accountNonLocked; // 계정 잠김 여부
    private boolean credentialsNonExpired; // 자격 증명 만료 여부
    private boolean enabled; // 계정 활성화 여부

    public void updateUserInfo(String userId, String userName, String encodedPassword) {
        this.userId = userId;
        this.userName = userName;
        this.password = encodedPassword;
    }
}
