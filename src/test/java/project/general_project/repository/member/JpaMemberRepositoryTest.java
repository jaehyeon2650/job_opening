package project.general_project.repository.member;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.general_project.domain.Member;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class JpaMemberRepositoryTest {
    @Autowired
    EntityManager em;

    @Autowired
    MemberRepository repository;

    @Test
    public void 회원가입() throws Exception{
        //given
        Member member=new Member();
        member.setUsername("asd");
        //when
        repository.save(member);
        //then
        Member findMember = repository.findById(member.getId());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void 회원_이름으로_찾기() throws Exception{
        //given
        Member member1=new Member();
        member1.setUserId("ind07142");
        Member member2=new Member();
        member2.setUserId("ind07152");
        Member member3=new Member();
        member3.setUserId("ind07162");
        Member member4=new Member();
        member4.setUserId("ind07172");
        repository.save(member1);
        repository.save(member2);
        repository.save(member3);
        repository.save(member4);
        //when
        List<String> names=new ArrayList<>();
        names.add("ind07142");
        names.add("ind07152");
        List<Member> membersByName = repository.findMembersByUserId(names);
        //then
        assertThat(membersByName.size()).isEqualTo(2);
    }
}