package seungil.login_boilerplate.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final UUID id; // 여기에 UUID 추가
    private final String email;
    private final String password;
    private final String username;
    private final boolean accountNonExpired; // 계정 만료 여부
    private final boolean accountNonLocked; // 계정 잠김 여부
    private final boolean credentialsNonExpired; // 자격 증명 만료 여부
    private final boolean enabled; // 계정 활성화 여부
    private final User user;

    public CustomUserDetails(UUID id, String email, String password, String username, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled, User user, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
        this.user = user;
        this.authorities = authorities;
    }

    private final Collection<? extends GrantedAuthority> authorities; // 사용자 권한 목록

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
