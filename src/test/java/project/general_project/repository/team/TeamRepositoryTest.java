package project.general_project.repository.team;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.general_project.domain.Member;
import project.general_project.domain.Team;
import project.general_project.repository.member.MemberRepository;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class TeamRepositoryTest {
    
    @Autowired
    EntityManager em;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 팀_생성() throws Exception{
        //given
        Team team=new Team();
        Member member1 = new Member();
        Member member2 = new Member();
        Member member3 = new Member();
        Member member4 = new Member();
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        team.addMember(member1);
        team.addMember(member2);
        team.addMember(member3);
        team.addMember(member4);
        teamRepository.save(team);
        em.flush();
        em.clear();
        //when
        Member memberFind1 = memberRepository.findByIdWithTeam(member1.getId());
        Member memberFind2 = memberRepository.findByIdWithTeam(member2.getId());
        Member memberFind3 = memberRepository.findByIdWithTeam(member3.getId());
        Member memberFind4 = memberRepository.findByIdWithTeam(member4.getId());

        //then
        assertThat(memberFind1.getTeam().getId()).isEqualTo(team.getId());
        assertThat(memberFind2.getTeam().getId()).isEqualTo(team.getId());
        assertThat(memberFind3.getTeam().getId()).isEqualTo(team.getId());
        assertThat(memberFind4.getTeam().getId()).isEqualTo(team.getId());
    }
    
    @Test
    public void 팀_삭제() throws Exception{
        //given
        Member member1 = new Member();
        Member member2 = new Member();
        Member member3 = new Member();
        Member member4 = new Member();
        Team team=new Team();
        teamRepository.save(team);
        team.addMember(member1);
        team.addMember(member2);
        team.addMember(member3);
        team.addMember(member4);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
        //when
        teamRepository.deleteTeam(team);
        em.flush();
        em.clear();
        Team teamById = teamRepository.getTeamById(team.getId());
        //then
        assertThat(teamById).isNull();
        assertThat(member1.getTeam()).isNull();
        assertThat(member2.getTeam()).isNull();
        assertThat(member3.getTeam()).isNull();
        assertThat(member4.getTeam()).isNull();
    }

    @Test
    public void 팀_삭제2() throws Exception{
        //given
        Member member1 = new Member();
        Member member2 = new Member();
        Member member3 = new Member();
        Member member4 = new Member();
        Team team=new Team();
        team.addMember(member1);
        team.addMember(member2);
        team.addMember(member3);
        team.addMember(member4);
        teamRepository.save(team);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        //when
        teamRepository.deleteTeam(team);
        em.flush();
        em.clear();
        Team teamById = teamRepository.getTeamById(team.getId());
        //then
        assertThat(teamById).isNull();
        assertThat(member1.getTeam()).isNull();
        assertThat(member2.getTeam()).isNull();
        assertThat(member3.getTeam()).isNull();
        assertThat(member4.getTeam()).isNull();
    }
    
    @Test
    public void 리더와_함께_팀_조회() throws Exception{
        //given
        Member member1 = new Member();
        Member member2 = new Member();
        Member member3 = new Member();
        Member member4 = new Member();
        Team team=new Team();
        team.addMember(member1);
        team.addMember(member2);
        team.addMember(member3);
        team.addMember(member4);
        team.setLeader(member1);
        teamRepository.save(team);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        em.flush();
        em.clear();
        //when
        System.out.println("===============================================");
        Team findTeam = teamRepository.getTeamByIdWithLeader(team.getId());
        System.out.println("===============================================");
        //then
        assertThat(findTeam.getLeader().getId()).isEqualTo(member1.getId());
    }
}