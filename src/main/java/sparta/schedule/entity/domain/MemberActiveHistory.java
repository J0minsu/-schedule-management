package sparta.schedule.entity.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import sparta.schedule.entity.common.audit.BaseTime;
import sparta.schedule.entity.common.enums.ActiveType;
import sparta.schedule.entity.common.enums.UserType;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;

/**
 * packageName    : sparta.schedule.entity.domain
 * fileName       : MemberActiveHistory
 * author         : ms.jo
 * date           : 2024. 6. 14.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024. 6. 14.        ms.jo       최초 생성
 */
@Entity
@Getter
@DynamicUpdate
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "member")
@Comment("멤버 (비)활성화 내역")
public class MemberActiveHistory extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("멤버 (비)활성화 내역 순차번호")
    Long seq;

    @Enumerated(EnumType.STRING)
    @Comment("(비)활성화 타입")
    ActiveType activeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_seq")
    Member member;

    @Comment("멤버 비활성화 사유")
    String reason;

    protected MemberActiveHistory(ActiveType activeType, Member member, String reason) {
        this.activeType = activeType;
        this.member = member;
        this.reason = reason;
    }

    public static MemberActiveHistory activeLog(Member member) {
        return new MemberActiveHistory(ActiveType.ACTIVE, member, null);
    }

    public static MemberActiveHistory inactiveLog(Member member, String reason) {
        return new MemberActiveHistory(ActiveType.INACTIVE, member, reason);
    }

}
