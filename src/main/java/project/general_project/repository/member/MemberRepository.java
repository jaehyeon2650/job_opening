package project.general_project.repository.member;

import project.general_project.domain.Member;
import project.general_project.domain.Team;

import java.util.List;
import java.util.Optional;


public interface MemberRepository {
    void save(Member member);
    Member findById(Long id);
    Optional<Member> findByUserID(String userId);
    Member findByIdWithTeam(Long id);
    List<Member> findMembersByUserId(List<String> ids);

    void setTeam(List<Long> ids, Team team);

}
