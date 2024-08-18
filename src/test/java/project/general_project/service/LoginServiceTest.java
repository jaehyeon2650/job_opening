package project.general_project.service;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.general_project.domain.Address;
import project.general_project.domain.Member;
import project.general_project.repository.member.MemberRepository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class LoginServiceTest {
    @Autowired
    EntityManager em;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    LoginService loginService;


    @Test
    public void 로그인_성공() throws Exception{
        //given
        Member member1=Member.createMember("asd","asd","asd","01026501404","ind07152@naver.com", Address.createAddress("asd","asd","asda"));
        Member member2=Member.createMember("asd","asdasd","asd","01026501404","ind07152@naver.com", Address.createAddress("asd","asd","asda"));
        memberService.save(member1);
        memberService.save(member2);
        //when
        Member loginMember = loginService.login(member1.getUserId(), "asd");

        //then
        assertThat(loginMember).isEqualTo(member1);
    }

    @Test
    public void 로그인_실패_비밀번호_x() throws Exception{
        //given
        Member member1=Member.createMember("asd","asd","asd","01026501404","ind07152@naver.com", Address.createAddress("asd","asd","asda"));
        Member member2=Member.createMember("asd","asdasd","asd","01026501404","ind07152@naver.com", Address.createAddress("asd","asd","asda"));
        memberService.save(member1);
        memberService.save(member2);
        //when
        Member loginMember = loginService.login(member1.getUserId(), "asd2");
        //then
        assertThat(loginMember).isNull();
    }

    @Test
    public void 로그인_실패_아이디_x() throws Exception{
        //given
        Member member1=Member.createMember("asd","asd","asd","01026501404","ind07152@naver.com", Address.createAddress("asd","asd","asda"));
        Member member2=Member.createMember("asd","asdasd","asd","01026501404","ind07152@naver.com", Address.createAddress("asd","asd","asda"));
        memberService.save(member1);
        memberService.save(member2);
        //when
        Member loginMember = loginService.login("asdas", "asd");
        //then
        assertThat(loginMember).isNull();
    }
}