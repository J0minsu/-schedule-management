package sparta.schedule.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import sparta.schedule.config.security.SecurityUserDetails;
import sparta.schedule.domain.member.service.MemberService;
import sparta.schedule.domain.schedule.dto.req.ScheduleChangeStatusReq;
import sparta.schedule.domain.schedule.dto.req.ScheduleCreateReq;
import sparta.schedule.domain.schedule.dto.res.ScheduleFindRes;
import sparta.schedule.domain.schedule.mapper.ScheduleMapper;
import sparta.schedule.domain.schedule.service.ScheduleService;
import sparta.schedule.entity.domain.Member;
import sparta.schedule.entity.domain.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@SecurityRequirement(name = "Bearer Authentication")
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "Bearer"
)
@RestController
@RequestMapping("/schedules")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Schedule API")
public class ScheduleController {

    MemberService memberService;
    ScheduleService scheduleService;
    ScheduleMapper scheduleMapper;

    /**
     * CREATE BLOCK
     */

    @Operation(summary = "Schedule 생성")
    @ApiResponses(value ={
        @ApiResponse(responseCode = "200", description = "생성 완료"),
        @ApiResponse(responseCode = "403", description = "만료된 토큰"),
        @ApiResponse(responseCode = "404", description = "message [MEMBER : 사용자 정보 일치하지 않음]"),
    }
    )
    @PostMapping
    public ResponseEntity addSchedule(@RequestBody ScheduleCreateReq request,
                                  @AuthenticationPrincipal SecurityUserDetails details) {

        Member member = memberService.findBySeq(details.getSeq()).get();

        Schedule Schedule = scheduleService.createSchedule(member, request);

        return ResponseEntity.ok(Schedule);

    }


    /**
     * READ BLOCK
     */

    @Operation(summary = "가장 최근 Schedule 조회")
    @ApiResponses(value ={
        @ApiResponse(responseCode = "200", description = "요청 성공"),
        @ApiResponse(responseCode = "403", description = "만료된 토큰"),
        @ApiResponse(responseCode = "404", description = "message [MEMBER : 사용자 정보 일치하지 않음]"),
    })
    @GetMapping("/lastest")
    public ResponseEntity findLastestSchedule(@AuthenticationPrincipal SecurityUserDetails details) {

        Member member = memberService.findBySeq(details.getSeq()).get();

        Schedule Schedule = scheduleService.findLastestSchedule(member);

        ScheduleFindRes result = scheduleMapper.toScheduleFindRes(Schedule);

        return ResponseEntity.ok(result);

    }

    @Operation(summary = "Schedule 목록 조회")
    @ApiResponses(value ={
        @ApiResponse(responseCode = "200", description = "요청 성공"),
        @ApiResponse(responseCode = "403", description = "만료된 토큰"),
        @ApiResponse(responseCode = "404", description = "message [MEMBER : 사용자 정보 일치하지 않음]"),
    })
    @GetMapping
    public ResponseEntity findSchedules(
            int size, int page,
            @AuthenticationPrincipal SecurityUserDetails details) {

        Member member = memberService.findBySeq(details.getSeq()).get();

        log.info("member :: {}", member);

        Page<Schedule> Schedules = scheduleService.findSchedules(member, PageRequest.of( page - 1 < 0 ? 0 : page -1, size));

        Page<ScheduleFindRes> result = Schedules.map(i -> scheduleMapper.toScheduleFindRes(i));

        return ResponseEntity.ok(result);

    }

    /**
     * UPDATE BLOCK
     */
    @Operation(summary = "Schedule 상태 변경")
    @ApiResponses(value ={
        @ApiResponse(responseCode = "200", description = "변경 완료"),
        @ApiResponse(responseCode = "403", description = "만료된 토큰"),
        @ApiResponse(responseCode = "404", description = "message [MEMBER : 사용자 정보 일치하지 않음], [Schedule : 해당 Schedule 가 존재하지 않음]"),
        @ApiResponse(responseCode = "400", description = "진행중 상태에서만 대기 상태로 변경 가능"),
    })
    @PatchMapping("/{seq}")
    public ResponseEntity changeToSchedule(
            @PathVariable Long seq,
            @RequestBody ScheduleChangeStatusReq request,
            @AuthenticationPrincipal SecurityUserDetails details) {

        Member member = memberService.findBySeq(details.getSeq()).get();

        Schedule afterSchedule = scheduleService.changeStatus(member, seq, request.getStatus());
        ScheduleFindRes result = scheduleMapper.toScheduleFindRes(afterSchedule);

        return ResponseEntity.ok(result);

    }


    /**
     * DELETE BLOCK
     */
    @Operation(summary = "Schedule 삭제")
    @ApiResponses(value ={
        @ApiResponse(responseCode = "200", description = "변경 완료"),
        @ApiResponse(responseCode = "403", description = "만료된 토큰"),
        @ApiResponse(responseCode = "404", description = "message [MEMBER : 사용자 정보 일치하지 않음], [Schedule : 해당 Schedule 가 존재하지 않음]"),
        @ApiResponse(responseCode = "400", description = "삭제 실패"),
    })
    @DeleteMapping("/{seq}")
    public ResponseEntity deleteSchedule(
            @PathVariable Long seq,
            @AuthenticationPrincipal SecurityUserDetails details) {

        Member member = memberService.findBySeq(details.getSeq()).get();

        scheduleService.deleteSchedule(member, seq);

        return ResponseEntity.ok().build();

    }


}
