package sparta.schedule.domain.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sparta.schedule.entity.domain.ScheduleHistory;

/**
 * packageName    : sparta.schedule.domain.schedule.repository
 * fileName       : ScheduleHistoryRepository
 * author         : ms.jo
 * date           : 2024. 6. 29.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024. 6. 29.        ms.jo       최초 생성
 */
@Repository
public interface ScheduleHistoryRepository extends JpaRepository<ScheduleHistory, Long> {
}
