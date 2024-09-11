package project.general_project.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.general_project.domain.Alarm;
import project.general_project.domain.Member;
import project.general_project.service.AlarmService;
import project.general_project.service.MemberService;
import project.general_project.web.form.alarmForm.AlarmForm;
import project.general_project.web.form.memberForm.Login;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class AlarmController {
    private final AlarmService alarmService;
    private final MemberService memberService;

    @GetMapping("/member/{id}/alarm")
    public String getAlarm(@Login Member member, @PathVariable Long id, Model model){
        if(!member.getId().equals(id)) return "redirect:/";
        List<Alarm> alarms = alarmService.findAlarmsByMemberId(id);
        List<AlarmForm> alarmForms = alarms.stream().map(o -> {
            return AlarmForm.createAlarmForm(o);
        }).collect(Collectors.toList());
        model.addAttribute("count",alarmService.getNotReadCount(id));
        model.addAttribute("alarmForms",alarmForms);
        model.addAttribute("memberId",member.getId());
        return "alarmForm";
    }

    @PostMapping("/member/{id}/alarm/edit")
    public String changeReadStatus(@Login Member member, @PathVariable Long id, RedirectAttributes redirectAttributes){
        if(!member.getId().equals(id)) return "redirect:/";
        alarmService.changeReadStateAll(id);
        redirectAttributes.addAttribute("id",id);
        return "redirect:/member/{id}/alarm";
    }

    @PostMapping("/member/{id}/alarm/delete")
    public String deleteAlarm(@Login Member member,@PathVariable Long id,RedirectAttributes redirectAttributes){
        if(!member.getId().equals(id)) return "redirect:/";
        alarmService.deleteAlarm(id);
        redirectAttributes.addAttribute("id",id);
        return "redirect:/member/{id}/alarm";
    }
}
