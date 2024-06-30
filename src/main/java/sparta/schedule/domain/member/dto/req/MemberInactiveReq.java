package sparta.schedule.domain.member.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * packageName    : sparta.schedule.domain.member.dto.req
 * fileName       : MemberInactiveReq
 * author         : ms.jo
 * date           : 2024. 6. 14.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024. 6. 14.        ms.jo       최초 생성
 */
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(title = "사용자 비활성화 요청 DTO")
@AllArgsConstructor
public class MemberInactiveReq {

    @Schema(example = "개인 사유로 인해 더이상 활동할 수 없어서")
    String reason;

}
