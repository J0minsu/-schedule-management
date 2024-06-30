package sparta.schedule.domain.schedule.repository;

import sparta.schedule.entity.domain.Member;
import sparta.schedule.entity.domain.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Page<Schedule> findByMemberOrderByCreatedAtDesc(Member member, Pageable pageable);

    Optional<Schedule> findByMemberAndSeq(Member member, Long seq);
}
