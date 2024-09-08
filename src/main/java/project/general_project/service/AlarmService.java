package project.general_project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.general_project.domain.Alarm;
import project.general_project.repository.alarm.AlarmRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmService {
    private final AlarmRepository repository;

    public Alarm findAlarmById(Long id){
        return repository.findById(id);
    }

    public List<Alarm> findAlarmsByMemberId(Long id){
        return repository.findAlarmsByMemberId(id);
    }

    @Transactional
    public void changeReadState(Long id){
        Alarm findAlarm = repository.findById(id);
        findAlarm.setRead(true);
    }

}
