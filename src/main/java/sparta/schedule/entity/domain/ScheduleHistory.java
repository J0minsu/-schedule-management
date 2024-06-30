package sparta.schedule.entity.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import sparta.schedule.entity.common.audit.BaseTime;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;
import sparta.schedule.entity.common.enums.HistoryType;
import sparta.schedule.util.JSONUtil;

/**
 * packageName    : sparta.schedule.entity.domain
 * fileName       : ScheduleHistory
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
@Comment("일정 변경 내역")
public class ScheduleHistory extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("일정 변경 내역 순차번호")
    Long seq;

    @Comment("일정 순차번호")
    Long scheduleSeq;

    @Enumerated(EnumType.STRING)
    HistoryType type;

    @Comment("변경 전 data")
    @Column(length = 2048)
    String beforeData;
    @Comment("변경 후 data")
    @Column(nullable = false, length = 2048)
    String afterData;

    protected ScheduleHistory(Long memberSeq, Long scheduleSeq, HistoryType historyType, String before, String after) {
        this.seq = memberSeq;
        this.scheduleSeq = scheduleSeq;
        this.type = historyType;
        this.beforeData = before;
        this.afterData = after;
    }

    public static ScheduleHistory create(Long memberSeq, Long scheduleSeq, HistoryType type, Schedule before, Schedule after) {

        ScheduleHistory history = new ScheduleHistory(memberSeq, scheduleSeq, type, JSONUtil.toJSONString(before), JSONUtil.toJSONString(after));

        return history;
    }

}
