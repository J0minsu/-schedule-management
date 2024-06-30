package sparta.schedule.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import sparta.schedule.domain.auth.dto.req.JwtIssueReq;
import sparta.schedule.domain.auth.dto.res.JwtIssueRes;
import sparta.schedule.domain.auth.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * packageName    : sparta.schedule.controller
 * fileName       : TokenController
 * author         : ms.jo
 * date           : 2024. 6. 13.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024. 6. 13.        ms.jo       최초 생성
 */
@RestController
@RequestMapping("/tokens")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequiredArgsConstructor
@Tag(name = "토큰 발급 API")
public class TokenController {

    TokenService tokenService;

    @Operation(summary = "JWT 발급")
    @ApiResponses(value ={
        @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = JwtIssueRes.class))}),
        @ApiResponse(responseCode = "401", description = "해당 api 접근 권한 없음"),
        @ApiResponse(responseCode = "404", description = "일치하는 사용자 없음")
    })
    @PostMapping
    public ResponseEntity issuesStaffJwt(@RequestBody JwtIssueReq jwtIssueReq) {

        JwtIssueRes result = tokenService.issueMemberJwt(jwtIssueReq.getId(), jwtIssueReq.getPassword());

        return ResponseEntity.ok().body(result);
    }

}
