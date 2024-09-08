package project.general_project.repository.alarm;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.general_project.domain.Alarm;
import project.general_project.domain.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class AlarmRepositoryTest {
    @Autowired
    EntityManager em;
    @Autowired
    AlarmRepository repository;

    @Test
    public void 알람_생성() throws Exception{
        //given
        Alarm alarm1=new Alarm();
        Alarm alarm2=new Alarm();
        repository.save(alarm1);
        repository.save(alarm2);
        //when
        Alarm findAlarm = repository.findById(alarm1.getId());
        //then
        assertThat(findAlarm).isNotNull();
    }

    @Test
    public void 회원으로_알람_조회하기() throws Exception{
        //given
        Member member1=new Member();
        Member member2=new Member();
        em.persist(member1);
        em.persist(member2);
        Alarm alarm1=new Alarm();
        Alarm alarm2=new Alarm();
        alarm1.setMember(member1);
        alarm2.setMember(member1);
        repository.save(alarm1);
        repository.save(alarm2);
        //when
        List<Alarm> findAlarms1 = repository.findAlarmsByMemberId(member1.getId());
        List<Alarm> findAlarms2 = repository.findAlarmsByMemberId(member2.getId());
        //then
        assertThat(findAlarms1.size()).isEqualTo(2);
        assertThat(findAlarms2.size()).isEqualTo(0);
    }


}