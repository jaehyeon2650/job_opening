package project.general_project.repository.team;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import project.general_project.domain.Member;
import project.general_project.domain.Team;

@Repository
public class TeamRepository {
    private final EntityManager em;
    private final JPAQueryFactory query;

    public TeamRepository(EntityManager em) {
        this.em=em;
        query=new JPAQueryFactory(em);
    }

    public void save(Team team){
        em.persist(team);
    }

    public Team getTeamById(Long id){
        return em.find(Team.class,id);
    }

    public void deleteTeam(Team team){
        em.createQuery("update Member m set m.team=null where m.team.id=:teamId")
                    .setParameter("teamId",team.getId())
                    .executeUpdate();
        for (Member member : team.getMembers()) {
            member.setTeam(null);
        }

        em.createQuery("delete from Team t where t.id=:teamId")
                .setParameter("teamId",team.getId())
                .executeUpdate();

        em.flush();
        em.clear();
    }

}
