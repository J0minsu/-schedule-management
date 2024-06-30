package sparta.schedule.domain.auth.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * packageName    : sparta.schedule.domain.auth.dto.req
 * fileName       : JwtIssueReq
 * author         : ms.jo
 * date           : 2024. 6. 14.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024. 6. 14.        ms.jo       최초 생성
 */
@Data
@Schema(title = "JWT 발급 요청 DTO")
public class JwtIssueReq {

    @Schema(description = "아이디", example = "msjo")
    private String id;

    @Schema(description = "비밀번호", example = "qwe123!@#")
    private String password;

}

