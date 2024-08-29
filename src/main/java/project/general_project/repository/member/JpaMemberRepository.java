package project.general_project.repository.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import project.general_project.domain.Member;

import java.util.List;
import java.util.Optional;

import static project.general_project.domain.QMember.*;

@Repository
public class JpaMemberRepository implements MemberRepository{
    private final EntityManager em;
    private final JPAQueryFactory query;

    public JpaMemberRepository(EntityManager em) {
        this.em = em;
        query=new JPAQueryFactory(em);
    }

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

    @Override
    public List<Member> findMembersByUserId(List<String> ids) {
        return query.selectFrom(member)
                .where(member.userId.in(ids))
                .fetch();
    }


}
