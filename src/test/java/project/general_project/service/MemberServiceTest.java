package project.general_project.service;


import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import project.general_project.domain.Address;
import project.general_project.domain.Member;
import project.general_project.repository.member.MemberRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired
    EntityManager em;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberService memberService;

    @Test
    public void 회원가입() throws Exception{
        //given
        Member member=new Member();
        member.setUsername("asd");
        member.setPassword("asdsa");
        //when
        memberService.save(member);
        //then
        Member findMember = memberService.findById(member.getId());
        assertThat(findMember).isEqualTo(member);
    }
    @Test
    public void 같은_아이디() throws Exception{
        //given
        Member member1=Member.createMember("asd","asd","asd","01026501404","ind07152@naver.com",Address.createAddress("asd","asd","asda"));
        memberService.save(member1);
        //when
        Member member2=Member.createMember("asd","asd","asd","01026501404","ind07152@naver.com",Address.createAddress("asd","asd","asda"));
        Long saveId = memberService.save(member2);
        //then
        assertThat(saveId).isEqualTo(-1L);
    }

    @Test
    public void 비밀번호_해쉬() throws Exception{
        //given
        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        Member member1=Member.createMember("asd","asd","asd","01026501404","ind07152@naver.com",Address.createAddress("asd","asd","asda"));
        memberService.save(member1);
        //when
        Member findMember = memberService.findById(member1.getId());
        //then
        boolean password = passwordEncoder.matches("asd",findMember.getPassword());
        assertThat(password).isTrue();
    }

    @Test
    public void 회원_수정() throws Exception{
        //given
        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        Member member1=Member.createMember("asd","asd","asd","01026501404","ind07152@naver.com",Address.createAddress("asd","asd","asda"));
        memberService.save(member1);
        //when
        member1.setPhone("01012341234");
        memberService.updateMember(member1);
        //then
        Member findMember = memberService.findById(member1.getId());
        assertThat(findMember).isEqualTo(member1);
    }
}