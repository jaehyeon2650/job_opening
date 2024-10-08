package project.general_project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.general_project.domain.Alarm;
import project.general_project.domain.Member;
import project.general_project.repository.alarm.AlarmRepository;
import project.general_project.repository.member.MemberRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmService {

    private final MemberRepository memberRepository;
    private final AlarmRepository repository;

    public Alarm findAlarmById(Long id) {
        return repository.findById(id);
    }

    public List<Alarm> findAlarmsByMemberId(Long id) {
        return repository.findAlarmsByMemberId(id);
    }

    @Transactional
    public void changeReadState(Long id) {
        Alarm findAlarm = repository.findById(id);
        findAlarm.setReadCheck(true);
    }

    @Transactional
    public void changeReadStateAll(Long id) {
        repository.changeReadStateAll(id);
    }

    @Transactional
    public Long save(Alarm alarm) {
        repository.save(alarm);
        return alarm.getId();
    }

    @Transactional
    public Long makeAlarmWithMember(Member member, Long fromMemberId, String content) {
        Alarm alarm = new Alarm(member, fromMemberId, content);
        repository.save(alarm);
        return alarm.getId();
    }

    @Transactional
    public Long makeAlarmWithMemberUserId(String userId, Long fromMemberId, String content) {
        Member member = memberRepository.findByUserID(userId).get();
        Alarm alarm = new Alarm(member, fromMemberId, content);
        repository.save(alarm);
        return alarm.getId();
    }

    @Transactional
    public void deleteAlarm(Long id) {
        repository.deleteAll(id);
    }

    public Long getNotReadCount(Long id) {
        return repository.notReadCount(id);
    }
}
