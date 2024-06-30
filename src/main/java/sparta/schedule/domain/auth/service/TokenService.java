package sparta.schedule.domain.auth.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import sparta.schedule.config.exception.BusinessException;
import sparta.schedule.domain.auth.dto.res.JwtIssueRes;
import sparta.schedule.domain.member.service.MemberService;
import sparta.schedule.entity.common.enums.UserType;
import sparta.schedule.entity.domain.Member;
import sparta.schedule.util.JwtUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

/**
 * packageName    : sparta.schedule.domain.auth.service
 * fileName       : TokenService
 * author         : ms.jo
 * date           : 2024. 6. 14.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024. 6. 14.        ms.jo       최초 생성
 */
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class TokenService {

    MemberService memberService;
    PasswordEncoder passwordEncoder;
    JwtUtil jwtUtil;
//    StringRedisTemplate stringRedisTemplate;

    private final Long EXPIRE_TIME = 1440L;

    public JwtIssueRes issueMemberJwt(String id, String rawPassword) {

        Member member = memberService.findById(id).orElseThrow(
                () -> new BusinessException(HttpStatus.NOT_FOUND, "MEMBER"));

        if (!passwordEncoder.matches(rawPassword, member.getPassword())) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED);
        }

        // Schedule 1440분 = 1일로 설정(리프레시 토큰 추가시 30분으로 변경)
        JwtIssueRes result = new JwtIssueRes(jwtUtil.createJwt(UserType.Member, member.getSeq(), member.getNickname(), EXPIRE_TIME));

//        stringRedisTemplate.opsForValue().set(result.getAccessToken(), "TRUE", Duration.ofMinutes(EXPIRE_TIME));

        return result;
    }

}