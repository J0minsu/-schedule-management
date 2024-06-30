package sparta.schedule.domain.member.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * packageName    : sparta.schedule.domain.member.dto.req
 * fileName       : MemberModifyNicknameReq
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
@Schema(title = "사용자 닉네임 변경 요청 DTO")
@AllArgsConstructor
public class MemberModifyNicknameReq {

    @Schema(example = "teresa")
    String nickname;

}
