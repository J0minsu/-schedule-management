package sparta.schedule.entity.domain;

import com.google.common.collect.Lists;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import sparta.schedule.entity.common.audit.BaseTime;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.List;

/**
 * packageName    : sparta.schedule.entity.domain.ScheduleHistory
 * fileName       : Member
 * author         : ms.jo
 * date           : 2024. 6. 13.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024. 6. 13.        ms.jo       최초 생성
 */
@Entity
@Getter
@DynamicUpdate
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Comment("사용자")
public class Member extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("사용자 순차번호")
    Long seq;

    @Comment("닉네임")
    String nickname;

    @Comment("논리 ID")
    String id;

    @Comment("로그인 패스워드")
    String password;

    @Comment("활성화 여부")
    boolean isActive = true;

    @Comment("비활성화 일시")
    LocalDateTime inactiveAt;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Schedule> schedules = Lists.newArrayList();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<MemberActiveHistory> activeHistories = Lists.newArrayList();

    private Member(String nickname, String id, String password) {
        this.nickname = nickname;
        this.id = id;
        this.password = password;
    }

    public static Member of(String nickname, String id, String password) {
        return new Member(nickname, id, password);
    }

    public static Member createEmptyObject() {
        return new Member();
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void makeInactive(String reason) {
        this.isActive = false;
        this.inactiveAt = LocalDateTime.now();
        this.getActiveHistories().add(MemberActiveHistory.inactiveLog(this, reason));
    }

    public void makeActive() {
        this.isActive = true;
        this.inactiveAt = null;
        this.getActiveHistories().add(MemberActiveHistory.activeLog(this));

    }

}
