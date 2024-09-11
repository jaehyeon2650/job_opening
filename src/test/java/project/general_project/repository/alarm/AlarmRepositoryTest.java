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

    @Test
    public void 읽지_않음_수_조회() throws Exception{
        //given
        Member member1=new Member();
        em.persist(member1);
        Alarm alarm1=new Alarm();
        Alarm alarm2=new Alarm();
        alarm1.setMember(member1);
        alarm1.setReadCheck(true);
        alarm2.setMember(member1);
        alarm2.setReadCheck(false);
        repository.save(alarm1);
        repository.save(alarm2);
        //when
        Long count = repository.notReadCount(member1.getId());
        //then
        assertThat(count).isEqualTo(1);
    }

    @Test
    public void 읽지_않음_수_조회2() throws Exception{
        //given
        Member member1=new Member();
        em.persist(member1);
        Alarm alarm1=new Alarm();
        Alarm alarm2=new Alarm();
        alarm1.setMember(member1);
        alarm1.setReadCheck(true);
        alarm2.setMember(member1);
        alarm2.setReadCheck(true);
        repository.save(alarm1);
        repository.save(alarm2);
        //when
        Long count = repository.notReadCount(member1.getId());
        //then
        assertThat(count).isEqualTo(0);
    }

    @Test
    public void 읽음으로_상태_변화() throws Exception{
        //given
        Member member1=new Member();
        em.persist(member1);
        Alarm alarm1=new Alarm();
        Alarm alarm2=new Alarm();
        alarm1.setMember(member1);
        alarm1.setReadCheck(false);
        alarm2.setMember(member1);
        alarm2.setReadCheck(false);
        repository.save(alarm1);
        repository.save(alarm2);
        //when
        repository.changeReadStateAll(member1.getId());
        Long count = repository.notReadCount(member1.getId());
        //then
        assertThat(count).isEqualTo(0);
    }

    @Test
    public void 모든_알람_삭제() throws Exception{
        //given
        Member member1=new Member();
        em.persist(member1);
        Alarm alarm1=new Alarm();
        Alarm alarm2=new Alarm();
        alarm1.setMember(member1);
        alarm1.setReadCheck(false);
        alarm2.setMember(member1);
        alarm2.setReadCheck(false);
        repository.save(alarm1);
        repository.save(alarm2);
        //when
        repository.deleteAll(member1.getId());
        List<Alarm> findAlarms = repository.findAlarmsByMemberId(member1.getId());
        //then
        assertThat(findAlarms.size()).isEqualTo(0);
    }
}