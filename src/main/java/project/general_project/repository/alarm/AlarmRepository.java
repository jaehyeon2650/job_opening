package project.general_project.repository.alarm;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import project.general_project.domain.Alarm;


import java.util.List;

import static project.general_project.domain.QAlarm.*;

@Repository
public class AlarmRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public AlarmRepository(EntityManager em) {
        this.em=em;
        this.query=new JPAQueryFactory(em);
    }

    public void save(Alarm alarm){
        em.persist(alarm);
    }

    public Alarm findById(Long id){
        return em.find(Alarm.class,id);
    }

    public List<Alarm> findAlarmsByMemberId(Long id){
        return query.selectFrom(alarm)
                .where(alarm.member.id.eq(id)).fetch();
    }
}
