package sparta.schedule.domain.member.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import sparta.schedule.config.security.SecurityUserInfo;
import sparta.schedule.entity.domain.Member;
import org.springframework.context.annotation.Configuration;

/**
 * packageName    : sparta.schedule.domain.member.mapper
 * fileName       : MemberMapper
 * author         : ms.jo
 * date           : 2024. 6. 14.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024. 6. 14.        ms.jo       최초 생성
 */
@Configuration
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberMapper {

    public SecurityUserInfo toSecurityUserInfo(Member member) {
        return new SecurityUserInfo(member.getId(), member.getPassword(), member.getSeq(), member.getNickname());
    }

}
