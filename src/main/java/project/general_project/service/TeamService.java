package project.general_project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.general_project.domain.Member;
import project.general_project.domain.Team;
import project.general_project.exception.NoTeamException;
import project.general_project.exception.NoUserException;
import project.general_project.exception.UserHasTeamException;
import project.general_project.repository.member.MemberRepository;
import project.general_project.repository.team.TeamRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {

    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long makeTeam(String teamName, Long leaderId, List<String> memberUserIds){
        Team team=new Team();
        team.setName(teamName);
        team.setLeader(memberRepository.findById(leaderId));
        List<Member> members = memberRepository.findMembersByUserId(memberUserIds);
        if(memberUserIds.size()!=members.size()) throw new NoUserException();
        List<Long> ids = makeMemberIdList(members);
        teamRepository.save(team);
        memberRepository.setTeam(ids,team);
        return team.getId();
    }

    @Transactional
    public void deleteTeam(Long teamId){
        Team findTeam = teamRepository.getTeamById(teamId);
        teamRepository.deleteTeam(findTeam);
    }

    @Transactional
    public Team findTeamById(Long teamId){
        return teamRepository.getTeamById(teamId);
    }

    @Transactional
    public void leaveTheTeam(Long memberId,Long teamId){
        Team team = teamRepository.getTeamById(teamId);
        if(team==null) throw new NoTeamException();
        if(team.getLeader().getId().equals(memberId)){
            teamRepository.deleteTeam(team);
        }else{
            Member findMember = memberRepository.findByIdWithTeam(memberId);
            findMember.setTeam(null);
            team.getMembers().removeIf(member -> member.getId()==memberId);
            if(team.getMembers().isEmpty()){
                teamRepository.deleteTeam(team);
            }
        }
    }

    @Transactional
    public void updateTeam(Long teamId,String teamName, List<String> members){
        Team team = teamRepository.getTeamById(teamId);
        teamRepository.resetTeamMembers(team);
        team.setName(teamName);
        List<Member> memberList = memberRepository.findMembersByUserId(members);
        if(members.size()!=memberList.size()){
            throw new NoUserException();
        }
        List<Long> ids = makeMemberIdList(memberList);
        memberRepository.setTeam(ids,team);
    }

    private List<Long> makeMemberIdList(List<Member> members){
        List<Long> list=new ArrayList<>();
        members.forEach(o->{
            list.add(o.getId());
        });
        return list;
    }
}
