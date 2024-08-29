package project.general_project.repository.member;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.general_project.domain.Member;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<Member> findByUserID(String userId){
        List<Member> members = em.createQuery("select m from Member m where m.userId=:userId", Member.class).setParameter("userId", userId)
                .getResultList();
        return members.stream().findAny();
    }

    @Override
    public Member findByIdWithTeam(Long id){
        return em.createQuery("select m from Member m join fetch Team t on m.id=:memberId",Member.class)
                .setParameter("memberId",id).getSingleResult();
    }
}
