package project.general_project.service;


import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.general_project.domain.Member;
import project.general_project.domain.Team;
import project.general_project.exception.NoUserException;
import project.general_project.repository.member.MemberRepository;
import project.general_project.repository.team.TeamRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class TeamServiceTest {
    @Autowired
    EntityManager em;

    @Autowired
    TeamService teamService;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 팀_생성() throws Exception{
        //given
        Member member1=new Member();
        member1.setUserId("a");
        Member member2=new Member();
        member2.setUserId("b");
        Member member3=new Member();
        member3.setUserId("c");
        Member member4=new Member();
        member4.setUserId("d");
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
        //when
        List<String> list=new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        Long teamId = teamService.makeTeam("asd", member1.getId(), list);
        Team findTeam = teamService.findTeamById(teamId);
        //then
        assertThat(member1.getTeam().getId()).isEqualTo(teamId);
        assertThat(member2.getTeam().getId()).isEqualTo(teamId);
        assertThat(member3.getTeam().getId()).isEqualTo(teamId);
        assertThat(member4.getTeam().getId()).isEqualTo(teamId);
        assertThat(findTeam).isNotNull();
    }

    @Test
    public void 팀_생성_예외() throws Exception{
        //given
        Member member1=new Member();
        member1.setUserId("a");
        Member member2=new Member();
        member2.setUserId("b");
        Member member3=new Member();
        member3.setUserId("c");
        Member member4=new Member();
        member4.setUserId("d");
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
        //when
        List<String> list=new ArrayList<>();
        list.add("a");
        list.add("asdsad");
        list.add("c");
        list.add("d");
        //then
        assertThrows(NoUserException.class,()-> teamService.makeTeam("asd", member1.getId(), list));
    }

    @Test
    public void 팀_삭제() throws Exception{
        //given
        Member member1=new Member();
        member1.setUserId("a");
        Member member2=new Member();
        member2.setUserId("b");
        Member member3=new Member();
        member3.setUserId("c");
        Member member4=new Member();
        member4.setUserId("d");
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
        //when
        List<String> list=new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        Long teamId = teamService.makeTeam("asd", member1.getId(), list);
        teamService.deleteTeam(teamId);
        //then
        assertThat(member1.getTeam()).isNull();
        assertThat(member2.getTeam()).isNull();
        assertThat(member3.getTeam()).isNull();
        assertThat(member4.getTeam()).isNull();
        Team teamFind = teamService.findTeamById(teamId);
        assertThat(teamFind).isNull();
    }

    @Test
    public void 팀_탈퇴1() throws Exception{
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
        teamService.leaveTheTeam(member2.getId(),team.getId());
        em.flush();
        em.clear();
        Team findTeam = teamService.findTeamById(team.getId());
        Member findMember = memberRepository.findById(member2.getId());
        //then
        assertThat(findTeam.getMembers().size()).isEqualTo(3);
        assertThat(findMember.getTeam()).isNull();
    }
    @Test
    public void 팀_탈퇴2() throws Exception{
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
        teamService.leaveTheTeam(member1.getId(),team.getId());
        em.flush();
        em.clear();
        Team findTeam = teamService.findTeamById(team.getId());
        Member findMember = memberRepository.findById(member2.getId());
        //then
        assertThat(findTeam).isNull();
        assertThat(findMember.getTeam()).isNull();
    }
}