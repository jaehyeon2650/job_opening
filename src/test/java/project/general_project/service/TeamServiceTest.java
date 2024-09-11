package project.general_project.service;


import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.general_project.domain.Alarm;
import project.general_project.domain.Member;
import project.general_project.domain.Team;
import project.general_project.exception.NoUserException;
import project.general_project.repository.alarm.AlarmRepository;
import project.general_project.repository.member.MemberRepository;
import project.general_project.repository.team.TeamRepository;

import java.lang.reflect.Array;
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
    @Autowired
    AlarmRepository alarmRepository;

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
        Member findMember1 = memberRepository.findById(member1.getId());
        Member findMember2 = memberRepository.findById(member2.getId());
        Member findMember3 = memberRepository.findById(member3.getId());
        Member findMember4 = memberRepository.findById(member4.getId());
        //then
        assertThat(findMember1.getTeam().getId()).isEqualTo(teamId);
        assertThat(findMember2.getTeam().getId()).isEqualTo(teamId);
        assertThat(findMember3.getTeam().getId()).isEqualTo(teamId);
        assertThat(findMember4.getTeam().getId()).isEqualTo(teamId);
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

    @Test
    public void 팀_삭제_알람() throws Exception{
        //given
        Member member1 = new Member();
        Member member2 = new Member();
        Team team=new Team();
        em.persist(member1);
        em.persist(member2);
        team.addMember(member1);
        team.addMember(member2);
        em.persist(team);
        //when
        teamService.deleteTeam(team.getId());
        List<Alarm> Member1Alarm = alarmRepository.findAlarmsByMemberId(member1.getId());
        List<Alarm> Member2Alarm = alarmRepository.findAlarmsByMemberId(member2.getId());
        //then
        assertThat(Member1Alarm.size()).isEqualTo(1);
        assertThat(Member2Alarm.size()).isEqualTo(1);
    }

    @Test
    public void 팀_탈퇴_알람() throws Exception{
        //given
        Member member1 = new Member();
        Member member2 = new Member();
        Member member3 = new Member();
        Team team=new Team();
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        team.addMember(member1);
        team.addMember(member2);
        team.addMember(member3);
        team.setLeader(member2);
        em.persist(team);
        //when
        teamService.leaveTheTeam(member1.getId(),team.getId());
        List<Alarm> Member1Alarm = alarmRepository.findAlarmsByMemberId(member1.getId());
        List<Alarm> Member2Alarm = alarmRepository.findAlarmsByMemberId(member2.getId());
        List<Alarm> Member3Alarm = alarmRepository.findAlarmsByMemberId(member3.getId());
        //then
        assertThat(Member1Alarm.size()).isEqualTo(0);
        assertThat(Member2Alarm.size()).isEqualTo(1);
        assertThat(Member3Alarm.size()).isEqualTo(1);
    }

    @Test
    public void 리더가_팀_탈퇴_알람() throws Exception{
        //given
        Member member1 = new Member();
        Member member2 = new Member();
        Member member3 = new Member();
        Team team=new Team();
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        team.addMember(member1);
        team.addMember(member2);
        team.addMember(member3);
        team.setLeader(member1);
        em.persist(team);
        //when
        teamService.leaveTheTeam(member1.getId(),team.getId());
        List<Alarm> Member1Alarm = alarmRepository.findAlarmsByMemberId(member1.getId());
        List<Alarm> Member2Alarm = alarmRepository.findAlarmsByMemberId(member2.getId());
        List<Alarm> Member3Alarm = alarmRepository.findAlarmsByMemberId(member3.getId());
        //then
        assertThat(Member1Alarm.size()).isEqualTo(1);
        assertThat(Member2Alarm.size()).isEqualTo(1);
        assertThat(Member3Alarm.size()).isEqualTo(1);
    }

    @Test
    public void 혼자_팀에_있을_때_탈퇴() throws Exception{
        //given
        Member member1 = new Member();
        Team team=new Team();
        em.persist(member1);
        team.addMember(member1);
        team.setLeader(member1);
        em.persist(team);
        //when
        teamService.leaveTheTeam(member1.getId(),team.getId());
        List<Alarm> Member1Alarm = alarmRepository.findAlarmsByMemberId(member1.getId());
        //then
        assertThat(Member1Alarm.size()).isEqualTo(1);
    }

    @Test
    public void 팀_생성_알람() throws Exception{
        //given
        Member member1=new Member();
        member1.setUserId("a");
        Member member2=new Member();
        member2.setUserId("b");
        Member member3=new Member();
        member3.setUserId("c");
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        List<String> memberIds=List.of("a","b","c");
        //when
        Long teamId = teamService.makeTeam("spring", member1.getId(), memberIds);
        List<Alarm> member1Alarms = alarmRepository.findAlarmsByMemberId(member1.getId());
        List<Alarm> member2Alarms = alarmRepository.findAlarmsByMemberId(member2.getId());
        List<Alarm> member3Alarms = alarmRepository.findAlarmsByMemberId(member3.getId());
        //then
        assertThat(member1Alarms.size()).isEqualTo(1);
        assertThat(member2Alarms.size()).isEqualTo(1);
        assertThat(member3Alarms.size()).isEqualTo(1);
    }

    @Test
    public void 팀_변경_알림() throws Exception{
        //given
        Member member1 = new Member();
        member1.setUserId("a");
        Member member2 = new Member();
        member2.setUserId("b");
        Member member3 = new Member();
        member3.setUserId("c");
        Member member4 = new Member();
        member4.setUserId("d");
        Team team=new Team();
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
        team.addMember(member1);
        team.addMember(member2);
        team.addMember(member3);
        team.setLeader(member1);
        em.persist(team);
        //when
        List<String> newMembers=List.of("a","b","d");
        teamService.updateTeam(team.getId(),"asd",newMembers);
        List<Alarm> member1Alarms = alarmRepository.findAlarmsByMemberId(member1.getId());
        List<Alarm> member2Alarms = alarmRepository.findAlarmsByMemberId(member2.getId());
        List<Alarm> member3Alarms = alarmRepository.findAlarmsByMemberId(member3.getId());
        List<Alarm> member4Alarms = alarmRepository.findAlarmsByMemberId(member4.getId());

        //then
        assertThat(member1Alarms.size()).isEqualTo(2);
        assertThat(member2Alarms.size()).isEqualTo(2);
        assertThat(member3Alarms.size()).isEqualTo(1);
        assertThat(member4Alarms.size()).isEqualTo(1);
    }

    @Test
    public void 팀_삭제_알림() throws Exception{
        //given
        Member member1 = new Member();
        member1.setUserId("a");
        Member member2 = new Member();
        member2.setUserId("b");
        Member member3 = new Member();
        member3.setUserId("c");
        Team team=new Team();
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        team.addMember(member1);
        team.addMember(member2);
        team.addMember(member3);
        team.setLeader(member1);
        em.persist(team);
        //when
        teamService.deleteTeam(team.getId());
        List<Alarm> member1Alarm = alarmRepository.findAlarmsByMemberId(member1.getId());
        List<Alarm> member2Alarm = alarmRepository.findAlarmsByMemberId(member2.getId());
        List<Alarm> member3Alarm = alarmRepository.findAlarmsByMemberId(member3.getId());
        //then
        assertThat(member1Alarm.size()).isEqualTo(3);
        assertThat(member1Alarm.get(0).getContent()).isNotNull();
        assertThat(member2Alarm.size()).isEqualTo(3);
        assertThat(member3Alarm.size()).isEqualTo(3);
    }

    @Test
    public void 리더_탈퇴로_팀_해체_메시지() throws Exception{
        //given
        Member member1 = new Member();
        member1.setUserId("a");
        Member member2 = new Member();
        member2.setUserId("b");
        Member member3 = new Member();
        member3.setUserId("c");
        Team team=new Team();
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        team.addMember(member1);
        team.addMember(member2);
        team.addMember(member3);
        team.setLeader(member1);
        em.persist(team);
        //when
        teamService.leaveTheTeam(member1.getId(),team.getId());
        List<Alarm> member1Alarm = alarmRepository.findAlarmsByMemberId(member1.getId());
        List<Alarm> member2Alarm = alarmRepository.findAlarmsByMemberId(member2.getId());
        List<Alarm> member3Alarm = alarmRepository.findAlarmsByMemberId(member3.getId());
        //then
        assertThat(member1Alarm.size()).isEqualTo(3);
        assertThat(member2Alarm.size()).isEqualTo(3);
        assertThat(member3Alarm.size()).isEqualTo(3);
        assertThat(member1Alarm.get(0).getFromMemberId()).isNull();
    }
}