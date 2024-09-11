package project.general_project.repository.assessment;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.general_project.domain.Assessment;
import project.general_project.domain.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class AssessmentRepositoryTest {
    @Autowired
    AssessmentRepository repository;
    @Autowired
    EntityManager em;

    @Test
    public void 평균_점수_조회() throws Exception{
        //given
        Member member=new Member();
        em.persist(member);
        Assessment assessment1=new Assessment();
        assessment1.setToMember(member);
        assessment1.setScore(10);
        Assessment assessment2=new Assessment();
        assessment2.setToMember(member);
        assessment2.setScore(20);
        Assessment assessment3=new Assessment();
        assessment3.setToMember(member);
        assessment3.setScore(30);
        repository.save(assessment1);
        repository.save(assessment2);
        repository.save(assessment3);
        //when
        double averageScore = repository.getAverageScore(member.getId());
        //then
        assertThat(averageScore).isEqualTo(20);
    }

    @Test
    public void 평가가_없을_때_평균_점수_조회() throws Exception{
        //given
        Member member=new Member();
        em.persist(member);
        //when
        double averageScore = repository.getAverageScore(member.getId());
        //then
        assertThat(averageScore).isEqualTo(0);
    }

    @Test
    public void 평가_개수() throws Exception{
        //given
        Member member=new Member();
        em.persist(member);
        Assessment assessment1=new Assessment();
        assessment1.setToMember(member);
        assessment1.setScore(10);
        Assessment assessment2=new Assessment();
        assessment2.setToMember(member);
        assessment2.setScore(20);
        Assessment assessment3=new Assessment();
        assessment3.setToMember(member);
        assessment3.setScore(30);
        repository.save(assessment1);
        repository.save(assessment2);
        repository.save(assessment3);
        //when
        Long count = repository.getAssessmentCount(member.getId());
        //then
        assertThat(count).isEqualTo(3);
    }

    @Test
    public void 평가가_없을_때_평가_개수() throws Exception{
        //given
        Member member=new Member();
        em.persist(member);
        //when
        Long count = repository.getAssessmentCount(member.getId());
        //then
        assertThat(count).isEqualTo(0);
    }

    @Test
    public void 모든_평가_조회() throws Exception{
        //given
        Member member=new Member();
        Member fromMember=new Member();
        em.persist(member);
        em.persist(fromMember);
        Assessment assessment1=new Assessment();
        assessment1.setToMember(member);
        assessment1.setFromMember(fromMember);
        assessment1.setScore(10);
        Assessment assessment2=new Assessment();
        assessment2.setToMember(member);
        assessment2.setFromMember(fromMember);
        assessment2.setScore(20);
        Assessment assessment3=new Assessment();
        assessment3.setToMember(member);
        assessment3.setScore(30);
        assessment3.setFromMember(fromMember);
        repository.save(assessment1);
        repository.save(assessment2);
        repository.save(assessment3);
        //when
        List<Assessment> assessments = repository.getAllAssessment(member.getId());
        //then
        assertThat(assessments.size()).isEqualTo(3);
        assertThat(assessments.get(0).getFromMember().getId()).isEqualTo(fromMember.getId());
    }

    @Test
    public void 평가가_없을_때_평가_조회() throws Exception{
        //given
        Member member=new Member();
        em.persist(member);
        //when
        List<Assessment> assessments = repository.getAllAssessment(member.getId());
        //then
        assertThat(assessments.size()).isEqualTo(0);
    }
}