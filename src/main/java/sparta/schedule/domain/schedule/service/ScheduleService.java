package sparta.schedule.domain.schedule.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import sparta.schedule.config.exception.BusinessException;
import sparta.schedule.domain.schedule.dto.req.ScheduleCreateReq;
import sparta.schedule.domain.schedule.repository.ScheduleHistoryRepository;
import sparta.schedule.domain.schedule.repository.ScheduleRepository;
import sparta.schedule.entity.common.enums.HistoryType;
import sparta.schedule.entity.common.enums.ScheduleStatus;
import sparta.schedule.entity.domain.Member;
import sparta.schedule.entity.domain.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sparta.schedule.entity.domain.ScheduleHistory;
import sparta.schedule.util.JSONUtil;

import java.util.Objects;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ScheduleService {

    ScheduleRepository scheduleRepository;
    ScheduleHistoryRepository scheduleHistoryRepository;

    public Schedule createSchedule(Member member, ScheduleCreateReq request) {

        Schedule schedule = Schedule.create(ScheduleStatus.TODO, request.getContents(), request.getComment(), request.getScheduledAt(), member);

        try {

            Schedule createdSchedule = createSchedule(schedule);
            ScheduleHistory history = ScheduleHistory.create(member.getSeq(), createdSchedule.getSeq(), HistoryType.CREATE, null, createdSchedule);

            createHistory(history);
            return schedule;
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return schedule.createEmptyObject();
        }

    }

    public Schedule createSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public ScheduleHistory createHistory(ScheduleHistory history) {
        return scheduleHistoryRepository.save(history);
    }

    @Transactional(readOnly = true)
    public Optional<Schedule> findById(Member member, Long seq) {
        return scheduleRepository.findByMemberAndSeq(member, seq);
    }

    @Transactional(readOnly = true)
    public Page<Schedule> findSchedules(Member member, Pageable pageable) {
        return scheduleRepository.findByMemberOrderByCreatedAtDesc(member, pageable);
    }

    public Schedule findLastestSchedule(Member member) {
        Page<Schedule> Schedules = findSchedules(member, PageRequest.of(0, 1));
        return Schedules.isEmpty() ? null : Schedules.getContent().get(0);
    }

    public Schedule changeStatus(Member member, Long seq, ScheduleStatus ScheduleStatus) {

        Schedule schedule = findById(member, seq).orElseThrow(() -> new BusinessException(HttpStatus.NO_CONTENT, "SCHEDULE"));

        if (Objects.isNull(schedule)) {
            throw new BusinessException(HttpStatus.NOT_FOUND, "SCHEDULE");
        }

        Schedule before = schedule.clone();

        switch (ScheduleStatus) {
            case TODO -> schedule.toTODO();
            case DONE -> schedule.toDone();
            case PENDING -> schedule.toPending();
            case IN_PROGRESS -> schedule.toProgress();
        }

        Schedule after = schedule.clone();

        createHistory(ScheduleHistory.create(member.getSeq(), after.getSeq(), HistoryType.MODIFY, before, after));

        return schedule;

    }

    @Transactional
    public void deleteSchedule(Member member, Long seq) {

        Schedule schedule = findById(member, seq)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "SCHEDULE"));

        schedule.prepareRemove();

        try {
            log.info("start");
            scheduleRepository.delete(schedule);
            log.info("end");
        }
        catch (RuntimeException e) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "SCHEDULE");
        }

        ScheduleHistory history = ScheduleHistory.create(member.getSeq(), schedule.getSeq(), HistoryType.DELETE, schedule, null);

        scheduleHistoryRepository.save(history);

    }
}
