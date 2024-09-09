package project.general_project.web.form.alarmForm;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import project.general_project.domain.Alarm;

import java.time.LocalDateTime;

@Data
@Getter @Setter
public class AlarmForm {
    private Long id;
    private String content;
    private boolean read;
    private LocalDateTime time;

    public static AlarmForm createAlarmForm(Alarm alarm){
        AlarmForm alarmForm=new AlarmForm();
        alarmForm.setId(alarmForm.getId());
        alarmForm.setContent(alarm.getContent());
        alarmForm.setTime(alarm.getTime());
        alarmForm.setRead(alarm.isReadCheck());
        return alarmForm;
    }
}
