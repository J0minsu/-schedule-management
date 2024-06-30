package sparta.schedule.domain.member.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import sparta.schedule.config.exception.BusinessException;
import sparta.schedule.domain.member.dto.req.MemberCreateReq;
import sparta.schedule.domain.member.dto.req.MemberInactiveReq;
import sparta.schedule.domain.member.dto.req.MemberModifyNicknameReq;
import sparta.schedule.domain.member.repository.MemberRepository;
import sparta.schedule.entity.domain.Member;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

/**
 * packageName    : sparta.schedule.domain.member.service
 * fileName       : MemberService
 * author         : ms.jo
 * date           : 2024. 6. 13.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024. 6. 13.        ms.jo       최초 생성
 */
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberService {

    MemberRepository memberRepository;
    PasswordEncoder passwordEncoder;

    public Member createUser(MemberCreateReq request) {

        if(!isDuplicateId(request.getId()) || !isDuplicateNickname(request.getNickname())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST);
        }

        request.setPassword(passwordEncoder.encode(request.getPassword()));

        Member member = Member.of(request.getNickname(), request.getId(), request.getPassword());

        try {
            return memberRepository.save(member);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return Member.createEmptyObject();
        }

    }

    /**
     * Schedule
     * Optional 로 반환할 지 Entity 로 반환할지
     *  - Optional 로 반환하고 호출한 곳에서 해당 결과에 따른 유동적인 처리
     *      => 타 Member 에 대해 조회할 여지가 있을 시
     *          (1. 스스로를 조회하는 상황 | 2. 타 Member 를 조회하는 상황  이 발생할 수 있다)
     *  - Entity 를 무조건 반환
     *      => 타 Member 에 대해 조회할 여지가 없을 시
     *          (JWT 파싱해서 가지고 온 seq 를 사용해 다시 Member 를 조회하는 건 비효율 적)
     * @param seq
     * @return
     */

    @Transactional(readOnly = true)
    public Optional<Member> findBySeq(Long seq) {
        return memberRepository.findBySeq(seq);
    }

    @Transactional(readOnly = true)
    public Optional<Member> findById(String id) {
        return memberRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Member> findByNickname(String nickname) {
        return memberRepository.findByNickname(nickname);
    }

    public boolean isDuplicateId(String id) {
        return findById(id).isEmpty();
    }

    public boolean isDuplicateNickname(String nickname) {
        return findByNickname(nickname).isEmpty();
    }

    @Transactional
    public void makeActive(Long memberSeq) {

        Member member = findBySeq(memberSeq).get();

        member.makeActive();

    }

    @Transactional
    public void makeInactive(Long memberSeq, MemberInactiveReq request) {

        Member member = findBySeq(memberSeq).get();

        member.makeInactive(request.getReason());

    }

    @Transactional
    public Member changeNickname(Long seq, MemberModifyNicknameReq request) {

        Member member = findBySeq(seq).get();
        member.changeNickname(request.getNickname());

        return member;

    }
}
