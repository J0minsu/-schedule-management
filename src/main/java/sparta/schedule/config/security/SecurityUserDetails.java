package sparta.schedule.config.security;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Slf4j
public class SecurityUserDetails implements UserDetails {

    private final SecurityUserInfo securityUserInfo;
    private final List<? extends GrantedAuthority> authorities;

    public SecurityUserDetails(SecurityUserInfo securityUserInfo, List<? extends GrantedAuthority> authorities) {
        this.securityUserInfo = securityUserInfo;
        this.authorities = authorities;
    }

    @Override
    public String getUsername() {
        return securityUserInfo.getId();
    }

    @Override
    public String getPassword() {
        return securityUserInfo.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getId() {
        return securityUserInfo.getId();
    }

    public Long getSeq() {
        return securityUserInfo.getSeq();
    }

    public String getNickname() {
        return securityUserInfo.getNickname();
    }

    /**
     * 필요시 재정의(기본 RETURN TRUE)
     */
    @Override
    public boolean isAccountNonExpired() {
        return Boolean.TRUE;
    }

    @Override
    public boolean isAccountNonLocked() {
        return Boolean.TRUE;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return Boolean.TRUE;
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE;
    }

}
