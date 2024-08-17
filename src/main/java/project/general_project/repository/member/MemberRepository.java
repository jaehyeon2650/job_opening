package project.general_project.repository.member;

import project.general_project.domain.Member;

import java.util.Optional;


public interface MemberRepository {
    void save(Member member);
    Member findById(Long id);
    Optional<Member> findByUserID(String userId);

}
