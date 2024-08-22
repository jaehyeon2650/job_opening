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
}