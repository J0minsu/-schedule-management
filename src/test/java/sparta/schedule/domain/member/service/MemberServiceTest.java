package sparta.schedule.domain.member.service;

import jakarta.persistence.EntityManager;
import sparta.schedule.config.exception.BusinessException;
import sparta.schedule.domain.member.dto.req.MemberCreateReq;
import sparta.schedule.domain.member.dto.req.MemberInactiveReq;
import sparta.schedule.entity.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * packageName    : sparta.schedule.domain.member.service
 * fileName       : MemberServiceTest
 * author         : ms.jo
 * date           : 2024. 6. 13.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024. 6. 13.        ms.jo       최초 생성
 */
@SpringBootTest
@Transactional(readOnly = true)
@ActiveProfiles("test")
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    EntityManager entityManager;

    @Test
    public void 사용자는_생성이_돼야한다() throws Exception {

        //given
        MemberCreateReq request = new MemberCreateReq("msjo", "one", "qwe123!@#");
        //when
        Member member = memberService.createUser(request);
        //then
        assertEquals(request.getId(), member.getId());

    }

    @Test
    public void 사용자는_생성_시_ID_및_Nickname이_다시_중복체크_돼야한다() throws Exception {

        //given
        MemberCreateReq request = new MemberCreateReq("msjo", "one", "qwe123!@#");
        Member member = memberService.createUser(request);

        //when
        assertThrows(BusinessException.class, () -> {
            memberService.createUser(request);
        });

    }

    @Test
    public void ID의_중복체크는_올바르게_수행돼야_한다() {

        //given
        MemberCreateReq request = new MemberCreateReq("msjo", "one", "qwe123!@#");
        //when
        Member member = memberService.createUser(request);
        //then
        memberService.findById(request.getId());

        assertEquals(false, memberService.isDuplicateId(request.getId()));

    }

    @Test
    public void 닉네임의_중복체크는_올바르게_수행돼야_한다() {

        //given
        MemberCreateReq request = new MemberCreateReq("msjo", "one", "qwe123!@#");
        //when
        Member member = memberService.createUser(request);
        //then
        memberService.findById(request.getId());

        assertEquals(false, memberService.isDuplicateNickname(request.getNickname()));

    }

    @Test
    public void 닉네임은_변경될_수_있어야_한다() throws Exception {
    
        //given
        MemberCreateReq request = new MemberCreateReq("msjo", "one", "qwe123!@#");
        Member member = memberService.createUser(request);
        //when
        String afterNickname = "two";

        member.changeNickname(afterNickname);

        Member afterMember = memberService.findById(request.getId()).get();

        //then
        assertEquals(afterNickname, afterMember.getNickname());

    }

    @Test
    public void 사용자는_비활성화_될_수_있어야_한다() throws Exception {
    
        //given
        MemberCreateReq request = new MemberCreateReq("msjo", "one", "qwe123!@#");
        Member member = memberService.createUser(request);
        //when
        boolean beforeIsActive = member.isActive();

        memberService.makeInactive(member.getSeq(), new MemberInactiveReq("더이상 활동 못할거같아서요"));

        Member afterMember = memberService.findById(request.getId()).get();
        boolean afterIsActive = afterMember.isActive();

        //then
        assertEquals(beforeIsActive, !afterIsActive);
        assertEquals(afterIsActive, false);

    }


    @Test
    public void 사용자는_활성화_될_수_있어야_한다() throws Exception {

        //given
        MemberCreateReq request = new MemberCreateReq("msjo", "one", "qwe123!@#");
        Member member = memberService.createUser(request);
        //when
        member.makeInactive("더이상 활동 못할거같아서요");
        boolean beforeIsActive = member.isActive();

        memberService.makeActive(member.getSeq());

        Member afterMember = memberService.findById(request.getId()).get();
        boolean afterIsActive = afterMember.isActive();

        //then
        assertEquals(beforeIsActive, !afterIsActive);
        assertEquals(afterIsActive, true);

    }
    
}