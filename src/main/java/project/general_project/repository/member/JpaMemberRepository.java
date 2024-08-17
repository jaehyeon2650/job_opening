package project.general_project.repository.member;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.general_project.domain.Member;

@Repository
@RequiredArgsConstructor
public class JpaMemberRepository implements MemberRepository{
    private final EntityManager em;
    @Override
    public void save(Member member) {
        em.persist(member);
    }

    @Override
    public Member findById(Long id) {
        return em.find(Member.class,id);
    }
}
