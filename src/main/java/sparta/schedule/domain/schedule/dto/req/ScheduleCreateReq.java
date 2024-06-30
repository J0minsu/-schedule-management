package sparta.schedule.domain.schedule.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(title = "Schedule 생성 요청 DTO")
@AllArgsConstructor
public class ScheduleCreateReq {

    @Schema(example = "프리킥 연습 30개")
    String contents;

    @Schema(example = "비오면 실내, 비 안오면 JC공원 에서")
    String comment;

    @Schema(example = "수행 예정 일시")
    LocalDateTime scheduledAt;

}
