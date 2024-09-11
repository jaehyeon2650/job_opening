package project.general_project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.general_project.domain.Member;
import project.general_project.domain.Team;
import project.general_project.exception.NoTeamException;
import project.general_project.exception.NoUserException;
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
    private final AlarmService alarmService;

    @Transactional
    public Long makeTeam(String teamName, Long leaderId, List<String> memberUserIds){
        Team team=new Team();
        team.setName(teamName);
        team.setLeader(memberRepository.findById(leaderId));
        List<Member> members = memberRepository.findMembersByUserId(memberUserIds);
        for (Member member : members) {
            alarmService.makeAlarmWithMember(member,null,team.getName()+" 팀에 합류했습니다.");
        }
        if(memberUserIds.size()!=members.size()) throw new NoUserException();
        List<Long> ids = makeMemberIdList(members);
        teamRepository.save(team);
        memberRepository.setTeam(ids,team);
        return team.getId();
    }

    @Transactional
    public void deleteTeam(Long teamId){
        Team findTeam = teamRepository.getTeamByIdWithMembers(teamId);
        alarmAfterDeleteTeam(findTeam.getMembers());
        alarmAfterUpdateTeam(findTeam.getMembers(),-1L,findTeam.getName()+"의 팀이 해체되었습니다.");
        teamRepository.deleteTeam(findTeam);
    }

    @Transactional
    public Team findTeamById(Long teamId){
        return teamRepository.getTeamById(teamId);
    }

    @Transactional
    public void leaveTheTeam(Long memberId,Long teamId){
        Team team = teamRepository.getTeamByIdWithMembers(teamId);
        if(team==null) throw new NoTeamException();
        if(team.getLeader().getId().equals(memberId)){
            alarmAfterDeleteTeam(team.getMembers());
            alarmAfterUpdateTeam(team.getMembers(),null,team.getName()+"의 팀이 해체되었습니다.");
            teamRepository.deleteTeam(team);
        }else{
            Member findMember = memberRepository.findByIdWithTeam(memberId);
            findMember.setTeam(null);
            team.getMembers().removeIf(member -> member.getId()==memberId);
            if(team.getMembers().isEmpty()){
                alarmAfterUpdateTeam(team.getMembers(),null,team.getName()+"의 팀이 해체되었습니다.");
                teamRepository.deleteTeam(team);
            }else{
                alarmAfterUpdateTeam(team.getMembers(),findMember.getId(),findMember.getUserId()+"님이 탈퇴하셨습니다.");
            }
        }
    }
    @Transactional
    public void updateTeam(Long teamId,String teamName, List<String> members){
        Team team = teamRepository.getTeamByIdWithMembers(teamId);
        List<Member> outMembers=getOutMembers(members,team);
        List<Member> stayMembers=getStayMembers(members,team);
        team.setName(teamName);
        alarmAfterUpdateTeam(outMembers,null,"팀에서 강제 퇴장 당하셨습니다.");
        boolean newMember= members.size() != stayMembers.size();
        for (Member outMember : outMembers) {
            alarmAfterUpdateTeam(stayMembers,outMember.getId(),outMember.getUserId()+"님이 탈퇴하셨습니다.");
            if(newMember) alarmAfterUpdateTeam(stayMembers,null,"새로운 맴버가 들어왔습니다.");
        }
        if(newMember){
            List<String> newMemberIds = getNewMemberIds(members, stayMembers);
            for (String newMemberId : newMemberIds) {
                alarmService.makeAlarmWithMemberUserId(newMemberId,null,team.getName()+" 팀에 합류하였습니다.");
            }

        }
        teamRepository.resetTeamMembers(team);
        List<Member> memberList = memberRepository.findMembersByUserId(members);
        List<Long> ids = makeMemberIdList(memberList);
        memberRepository.setTeam(ids,team);
    }

    private void alarmAfterUpdateTeam(List<Member> members,Long fromMember,String content){
        for (Member member : members) {
            alarmService.makeAlarmWithMember(member,fromMember,content);
        }
    }

    private void alarmAfterDeleteTeam(List<Member> members){
        for (Member toMember : members) {
            for (Member fromMember : members) {
                if(toMember.getId().equals(fromMember.getId())) continue;
                alarmService.makeAlarmWithMember(toMember,fromMember.getId(),fromMember.getUserId()+"님이 탈퇴하셨습니다.");
            }
        }
    }

    private List<Member> getStayMembers(List<String> members,Team team){
        List<Member> stayMember=new ArrayList<>();
        for (Member member : team.getMembers()) {
            if(members.contains(member.getUserId())){
                stayMember.add(member);
            }
        }
        return stayMember;
    }
    private List<Member> getOutMembers(List<String> members,Team team){
        List<Member> outMember=new ArrayList<>();
        for (Member member : team.getMembers()) {
            if(!members.contains(member.getUserId())){
                outMember.add(member);
            }
        }
        return outMember;
    }
    private List<String> getNewMemberIds(List<String> members,List<Member> stayMember){
        List<String> newMembers=new ArrayList<>();
        for (String member : members) {
            boolean check=true;
            for (Member teamMember : stayMember) {
                if(teamMember.getUserId().equals(member)) {
                    check=false;
                    break;
                }
            }
            if(check) newMembers.add(member);
        }
        return newMembers;
    }
    private List<Long> makeMemberIdList(List<Member> members){
        List<Long> list=new ArrayList<>();
        members.forEach(o->{
            list.add(o.getId());
        });
        return list;
    }
}
