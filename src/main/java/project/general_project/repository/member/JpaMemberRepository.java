package project.general_project.repository.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import project.general_project.domain.Member;
import project.general_project.domain.Team;
import project.general_project.exception.NoUserException;
import project.general_project.exception.UserHasTeamException;

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
        List<Member> members = em.createQuery("select m from Member m left join fetch m.team where m.userId=:userId", Member.class).setParameter("userId", userId)
                .getResultList();
        return members.stream().findAny();
    }

    @Override
    public Member findByIdWithTeam(Long id){
        return em.createQuery("select m from Member m left join fetch m.team where m.id=:memberId",Member.class)
                .setParameter("memberId",id).getSingleResult();
    }

    @Override
    public List<Member> findMembersByUserId(List<String> ids) {
        List<Member> members = query.selectFrom(member)
                .where(member.userId.in(ids))
                .fetch();
        if(members.size()!=ids.size()) throw new NoUserException();
        return members;
    }

    @Override
    public void setTeam(List<Long> ids, Team team) {
        int count=em.createQuery("update Member m set m.team=:team where m.id in :list and m.team is null")
                .setParameter("team",team)
                .setParameter("list",ids)
                .executeUpdate();
        if(count!=ids.size()) throw new UserHasTeamException();
        em.flush();
        em.clear();
    }


}
