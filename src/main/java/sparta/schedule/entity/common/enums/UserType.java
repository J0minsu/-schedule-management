package sparta.schedule.entity.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : sparta.schedule.entity.common.enums
 * fileName       : UserType
 * author         : ms.jo
 * date           : 2024. 6. 14.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024. 6. 14.        ms.jo       최초 생성
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserType {
    Member("서비스 사용자"),
    Admin("관리자"),
    System("시스템"),
    ;

    private String korean;

}
