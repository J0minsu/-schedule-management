package sparta.schedule.domain.auth.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * packageName    : sparta.schedule.domain.auth.dto.res
 * fileName       : JwtIssueRes
 * author         : ms.jo
 * date           : 2024. 6. 14.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024. 6. 14.        ms.jo       최초 생성
 */
@Data
@AllArgsConstructor
@Schema(title = "JWT 발급 응답 DTO")
public class JwtIssueRes {

    @Schema(description = "Access Token(JWT)", example = "eyJhbGciOiJIUzI1NiJ9.eyJhdXRoVHlwZSI6IlNUQUZGIiwiaWQiOiJkb2N0b3IxIiwibmFtZSI6Iuq5gOydmOyCrCIsInNlcSI6MiwiaG9zcGl0YWxTZXEiOjEsImlhdCI6MTcxNTA1NjkxMCwiZXhwIjoxNzE1MTQzMzEwfQ.EfRhysxN5V-Wy3m36yerNnnup0isr-nq8bMTfIduwZM")
    private String accessToken;

}

