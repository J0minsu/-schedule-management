package sparta.schedule.config.security;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import sparta.schedule.config.exception.BusinessException;
import sparta.schedule.domain.member.mapper.MemberMapper;
import sparta.schedule.domain.member.service.MemberService;
import sparta.schedule.entity.domain.Member;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityDetailsService implements UserDetailsService {

    MemberService memberService;
    MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

        SecurityUserInfo securityUserInfo = getSecurityUserInfo(id);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        return new SecurityUserDetails(securityUserInfo, authorities);
    }

    private SecurityUserInfo getSecurityUserInfo(String seq) {

        Member member = memberService.findBySeq(Long.parseLong(seq)).orElseThrow(
                () -> new BusinessException(HttpStatus.NOT_FOUND, "MEMBER"));

        if(!member.isActive()) {
            throw new BusinessException(HttpStatus.FORBIDDEN);
        }

        SecurityUserInfo securityUserInfo = memberMapper.toSecurityUserInfo(member);
        return securityUserInfo;

    }

}
