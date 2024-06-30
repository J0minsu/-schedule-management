package sparta.schedule.domain.schedule.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import sparta.schedule.domain.schedule.dto.res.ScheduleFindRes;
import sparta.schedule.entity.domain.Schedule;
import org.springframework.context.annotation.Configuration;

@Configuration
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleMapper {

    public ScheduleFindRes toScheduleFindRes(Schedule Schedule) {
        return new ScheduleFindRes(Schedule.getSeq(), Schedule.getContents(), Schedule.getComment(), Schedule.getStatus(), Schedule.getScheduledAt());
    }

}
