package project.general_project.repository.member;

import project.general_project.domain.Member;


public interface MemberRepository {
    void save(Member member);
    Member findById(Long id);

}
