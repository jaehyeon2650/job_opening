package project.general_project.repository.assessment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import project.general_project.domain.Assessment;

import java.util.List;

import static project.general_project.domain.QAssessment.assessment;

@Repository
public class AssessmentRepository {
    private final EntityManager em;
    private final JPAQueryFactory query;

    public AssessmentRepository(EntityManager em) {
        this.em = em;
        query = new JPAQueryFactory(em);
    }

    public void save(Assessment assessment) {
        em.persist(assessment);
    }

    public double getAverageScore(Long memberId) {
        Double result = em.createQuery("select avg(a.score) from Assessment a where a.toMember.id = :id", Double.class)
                .setParameter("id", memberId)
                .getSingleResult();
        return result != null ? result : 0.0;
    }

    public Long getAssessmentCount(Long memberId) {
        Long count = (Long) em.createQuery("select count(a) from Assessment a where a.toMember.id=:id")
                .setParameter("id", memberId)
                .getSingleResult();
        return count != null ? count : 0;
    }

    public List<Assessment> getAllAssessment(Long memberId) {
        return query.selectFrom(assessment)
                .leftJoin(assessment.fromMember).fetchJoin()
                .where(assessment.toMember.id.eq(memberId))
                .fetch();
    }
}
