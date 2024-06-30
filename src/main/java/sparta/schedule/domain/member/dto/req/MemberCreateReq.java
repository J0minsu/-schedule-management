package sparta.schedule.domain.member.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * packageName    : sparta.schedule.domain.member.dto.req
 * fileName       : MemberCreateReq
 * author         : ms.jo
 * date           : 2024. 6. 13.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024. 6. 13.        ms.jo       최초 생성
 */
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(title = "사용자 생성 DTO")
@AllArgsConstructor
public class MemberCreateReq {

    @NotEmpty(message = "id must be not null or empty")
    @Schema(example = "msjo")
    String id;

    @NotEmpty(message = "nickname must be not null or empty")
    @Schema(example = "jo")
    String nickname;

    @NotEmpty(message = "password must be not null or empty")
    @Schema(example = "qwe123!@#")
    String password;

}
