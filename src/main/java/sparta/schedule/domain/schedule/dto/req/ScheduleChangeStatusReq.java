package sparta.schedule.domain.schedule.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import sparta.schedule.entity.common.enums.ScheduleStatus;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(title = "Schedule 상태 변경 요청 DTO")
@AllArgsConstructor
public class ScheduleChangeStatusReq {

    @Schema(example = "TODO")
    ScheduleStatus status;

}
