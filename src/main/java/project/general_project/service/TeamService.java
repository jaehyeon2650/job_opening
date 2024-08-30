package project.general_project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.general_project.domain.Member;
import project.general_project.domain.Team;
import project.general_project.exception.NoUserException;
import project.general_project.exception.UserHasTeamException;
import project.general_project.repository.member.MemberRepository;
import project.general_project.repository.team.TeamRepository;

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
        for (Member member : members) {
            if(member.getTeam()!=null) throw new UserHasTeamException();
            team.addMember(member);
        }
        teamRepository.save(team);
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
}
