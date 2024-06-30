package sparta.schedule.domain.schedule.service;

import jakarta.persistence.EntityManager;
import org.springframework.http.HttpStatus;
import sparta.schedule.config.exception.BusinessException;
import sparta.schedule.domain.member.dto.req.MemberCreateReq;
import sparta.schedule.domain.member.service.MemberService;
import sparta.schedule.domain.schedule.dto.req.ScheduleCreateReq;
import sparta.schedule.entity.common.enums.ScheduleStatus;
import sparta.schedule.entity.domain.Member;
import sparta.schedule.entity.domain.Schedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional(readOnly = true)
@ActiveProfiles("test")
class ScheduleServiceTest {

    @Autowired MemberService memberService;
    @Autowired ScheduleService scheduleService;
    @Autowired EntityManager entityManager;

    Member member;

    @BeforeEach
    public void setting() {
        MemberCreateReq request = new MemberCreateReq("msjo", "one", "qwe123!@#");
        Member member = memberService.createUser(request);
        this.member = member;
    }

    @Test
    public void 사용자는_Schedule을_N개_생성할_수_있어야_한다() throws Exception {

        Schedule schedule1 = scheduleService.createSchedule(member,
                new ScheduleCreateReq("패널티킥 30번 차기", "실내 축구장 이용", LocalDateTime.now().plusDays(1))
        );

        Schedule schedule2 = scheduleService.createSchedule(member,
                new ScheduleCreateReq("패널티킥 30번 차기", "실내 축구장 이용", LocalDateTime.now().plusDays(1))
        );

        //then
        assertNotNull(schedule1);
        assertNotNull(schedule2);

    }

    @Test
    public void 사용자는_자신이_가장_최근에_작성한_Schedule을_조회할_수_있어야_한다() throws Exception {

        scheduleService.createSchedule(member,
                new ScheduleCreateReq("패널티킥 30번 차기", "실내 축구장 이용", LocalDateTime.now().plusDays(1))
        );

        Thread.sleep(1000 * 1);

        Schedule schedule2 = scheduleService.createSchedule(member,
                new ScheduleCreateReq("패널티킥 40번 차기", "실내 축구장 이용", LocalDateTime.now().plusDays(1))
        );

        Schedule findlastestSchedule = scheduleService.findLastestSchedule(member);

        assertEquals(findlastestSchedule.getContents(), schedule2.getContents());

        //then

    }

    @Test
    public void 사용자는_자신이_작성한_Schedule의_목록을_조회할_수_있어야_한다() throws Exception {

        IntStream.range(1, 8).forEach(i ->
            scheduleService.createSchedule(member,
                new ScheduleCreateReq("패널티킥 " + i + "0번 차기", "실내 축구장 이용", LocalDateTime.now().plusDays(1))
            )
        );

        //when
        Page<Schedule> schedules = scheduleService.findSchedules(member, PageRequest.of(2, 3));

        //then
        assertEquals(schedules.getTotalElements(), 7);
        assertEquals(schedules.getContent().size(), 1);
    }

    @Test
    public void CAN_Schedule_TO_DONE() throws Exception {

        //given
        Schedule schedule = scheduleService.createSchedule(member,
                new ScheduleCreateReq("패널티킥 30번 차기", "실내 축구장 이용", LocalDateTime.now().plusDays(1))
        );
        Long seq = schedule.getSeq();

        schedule.toDone();
        Schedule doneSchedule = scheduleService.findById(member, schedule.getSeq()).get();
        assertEquals(ScheduleStatus.DONE, doneSchedule.getStatus());

    }

    @Test
    public void CAN_Schedule_TO_PROGRESS() throws Exception {

        //given
        Schedule schedule = scheduleService.createSchedule(member,
                new ScheduleCreateReq("패널티킥 30번 차기", "실내 축구장 이용", LocalDateTime.now().plusDays(1))
        );
        Long seq = schedule.getSeq();

        schedule.toProgress();
        Schedule progressSchedule = scheduleService.findById(member, schedule.getSeq()).get();
        assertEquals(ScheduleStatus.IN_PROGRESS, progressSchedule.getStatus());

    }

    @Test
    public void CANNOT_Schedule_TO_PENDING() throws Exception {

        //given
        Schedule schedule = scheduleService.createSchedule(member,
                new ScheduleCreateReq("패널티킥 30번 차기", "실내 축구장 이용", LocalDateTime.now().plusDays(1))
        );
        Long seq = schedule.getSeq();

        assertThrows(BusinessException.class, () -> schedule.toPending());

    }

    @Test
    public void CAN_PROGRESS_TO_TODO() throws Exception {

        //given
        Schedule schedule = scheduleService.createSchedule(member,
                new ScheduleCreateReq("패널티킥 30번 차기", "실내 축구장 이용", LocalDateTime.now().plusDays(1))
        );
        Long seq = schedule.getSeq();
        schedule.toProgress();

        schedule.toTODO();
        assertEquals(ScheduleStatus.TODO, schedule.getStatus());

    }

    @Test
    public void CAN_PROGRESS_TO_DONE() throws Exception {

        //given
        Schedule schedule = scheduleService.createSchedule(member,
                new ScheduleCreateReq("패널티킥 30번 차기", "실내 축구장 이용", LocalDateTime.now().plusDays(1))
        );
        Long seq = schedule.getSeq();
        schedule.toProgress();

        schedule.toDone();
        assertEquals(ScheduleStatus.DONE, schedule.getStatus());

    }

    @Test
    public void CAN_PROGRESS_TO_PENDING() throws Exception {

        //given
        Schedule schedule = scheduleService.createSchedule(member,
                new ScheduleCreateReq("패널티킥 30번 차기", "실내 축구장 이용", LocalDateTime.now().plusDays(1))
        );
        Long seq = schedule.getSeq();
        schedule.toProgress();

        schedule.toPending();
        assertEquals(ScheduleStatus.PENDING, schedule.getStatus());

    }

    @Test
    public void CAN_DONE_TO_TODO() throws Exception {

        //given
        Schedule schedule = scheduleService.createSchedule(member,
                new ScheduleCreateReq("패널티킥 30번 차기", "실내 축구장 이용", LocalDateTime.now().plusDays(1))
        );
        Long seq = schedule.getSeq();
        schedule.toDone();

        schedule.toTODO();
        assertEquals(ScheduleStatus.TODO, schedule.getStatus());

    }

    @Test
    public void CAN_DONE_TO_PROGRESS() throws Exception {

        //given
        Schedule schedule = scheduleService.createSchedule(member,
                new ScheduleCreateReq("패널티킥 30번 차기", "실내 축구장 이용", LocalDateTime.now().plusDays(1))
        );
        Long seq = schedule.getSeq();
        schedule.toDone();

        schedule.toProgress();
        assertEquals(ScheduleStatus.IN_PROGRESS, schedule.getStatus());

    }

    @Test
    public void CANNOT_DONE_TO_PENDING() throws Exception {

        //given
        Schedule schedule = scheduleService.createSchedule(member,
                new ScheduleCreateReq("패널티킥 30번 차기", "실내 축구장 이용", LocalDateTime.now().plusDays(1))
        );
        Long seq = schedule.getSeq();
        schedule.toDone();

        assertThrows(BusinessException.class, () -> schedule.toPending());

    }

    @Test
    public void CAN_PENDING_TO_TODO() throws Exception {

        //given
        Schedule schedule = scheduleService.createSchedule(member,
                new ScheduleCreateReq("패널티킥 30번 차기", "실내 축구장 이용", LocalDateTime.now().plusDays(1))
        );
        Long seq = schedule.getSeq();
        schedule.toProgress();
        schedule.toPending();

        schedule.toTODO();
        assertEquals(ScheduleStatus.TODO, schedule.getStatus());

    }
    @Test
    public void CAN_PENDING_TO_PROGRESS() throws Exception {

        //given
        Schedule schedule = scheduleService.createSchedule(member,
                new ScheduleCreateReq("패널티킥 30번 차기", "실내 축구장 이용", LocalDateTime.now().plusDays(1))
        );
        Long seq = schedule.getSeq();
        schedule.toProgress();
        schedule.toPending();

        schedule.toProgress();
        assertEquals(ScheduleStatus.IN_PROGRESS, schedule.getStatus());

    }
    @Test
    public void CAN_PENDING_TO_DONE() throws Exception {

        //given
        Schedule schedule = scheduleService.createSchedule(member,
                new ScheduleCreateReq("패널티킥 30번 차기", "실내 축구장 이용", LocalDateTime.now().plusDays(1))
        );
        Long seq = schedule.getSeq();
        schedule.toProgress();
        schedule.toPending();

        schedule.toDone();
        assertEquals(ScheduleStatus.DONE, schedule.getStatus());

    }

    
    @Test
    public void 존재하지_않는_일정의_삭제는_올바르게_이행되지_않아야한다() throws Exception {
        
        Schedule schedule = scheduleService.createSchedule(member,
                new ScheduleCreateReq("패널티킥 30번 차기", "실내 축구장 이용", LocalDateTime.now().plusDays(1))
        );

        System.out.println("schedule = " + schedule);

        scheduleService.deleteSchedule(member, schedule.getSeq());

        entityManager.flush();
        entityManager.clear();

        assertThrows(BusinessException.class, () -> {
            scheduleService.deleteSchedule(member, schedule.getSeq());
        });

    }



}