package sparta.schedule.entity.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import sparta.schedule.config.exception.BusinessException;
import sparta.schedule.entity.common.audit.BaseTime;
import sparta.schedule.entity.common.enums.ScheduleStatus;
import sparta.schedule.util.JSONUtil;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Getter
@DynamicUpdate
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "member")
@Comment("일정")
@Slf4j
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Schedule extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("일정 순차번호")
    Long seq;

    @Enumerated(EnumType.STRING)
    @Comment("일정 상태")
    ScheduleStatus status;

    @Comment("내용")
    String contents;

    @Comment("코멘트")
    String comment;

    @Comment("수행 예정 일시")
    LocalDateTime scheduledAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_seq")
    @Comment("사용자")
    //prevent infinite recursive
    @JsonIgnore
    Member member;

    protected Schedule(ScheduleStatus status, String contents, String comment, LocalDateTime scheduledAt, Member member) {
        this.status = status;
        this.contents = contents;
        this.comment = comment;
        this.scheduledAt = scheduledAt;
        this.member = member;
    }

    public static Schedule create(ScheduleStatus status, String contents, String comment, LocalDateTime scheduledAt, Member member) {

        Schedule Schedule = new Schedule(status, contents, comment, scheduledAt, member);

        return Schedule;

    }

    public static Schedule createEmptyObject() {
        return new Schedule();
    }

    /**
     * Schedule history 관리를 어떻게 할 지 고민 중. 남긴다면 history 생성은 이 곳에서만. -- DONE
     *
     * @return
     */


    /**
     * State pattern
     * @return
     */
    public boolean toTODO() {
        return updateStatus(ScheduleStatus.TODO);
    }

    public boolean toProgress() {
        return updateStatus(ScheduleStatus.IN_PROGRESS);
    }

    public boolean toDone() {
        return updateStatus(ScheduleStatus.DONE);
    }

    public boolean toPending() {
        if (Objects.equals(this.status, ScheduleStatus.IN_PROGRESS)) {
            return updateStatus(ScheduleStatus.PENDING);
        }

        log.error("Changing to the Pending state is only possible when in the Progress state.");

        throw new BusinessException(HttpStatus.BAD_REQUEST);
    }

    private boolean updateStatus(ScheduleStatus newStatus) {
        String before = JSONUtil.toJSONString(this);
        this.status = newStatus;
        String after = JSONUtil.toJSONString(this);

        return true;
    }

    public Schedule clone() {
        return new Schedule(this.seq, this.status, this.contents, this.comment, this.scheduledAt, this.member);
    }

    public void prepareRemove() {
        this.member = null;
    }

}
