package project.general_project.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import project.general_project.domain.Member;
import project.general_project.service.AlarmService;
import project.general_project.service.AssessmentService;
import project.general_project.service.MemberService;
import project.general_project.web.form.assessmentForm.AssessmentForm;
import project.general_project.web.form.memberForm.Login;

@Controller
@RequiredArgsConstructor
public class AssessmentController {

    private final AssessmentService assessmentService;
    private final AlarmService alarmService;
    private final MemberService memberService;

    @GetMapping("/alarm/{alarmId}/assessment/{id}")
    public String assessmentForm(@Login Member member, @PathVariable Long id, Model model){
        Member toMember = memberService.findById(id);
        Member fromMember = memberService.findById(member.getId());
        AssessmentForm form=new AssessmentForm(toMember.getUserId(),fromMember.getUserId());
        model.addAttribute("assessmentForm",form);
        return "assessment/addAssessmentForm";
    }

    @PostMapping("/alarm/{alarmId}/assessment/{id}")
    public String addAssessment(@Validated @ModelAttribute("assessmentForm") AssessmentForm form, BindingResult bindingResult,@PathVariable("alarmId") Long id){
        if(bindingResult.hasErrors()){
            return "assessment/addAssessmentForm";
        }
        Member toMember = memberService.findByUserId(form.getToMemberId());
        Member fromMember = memberService.findByUserId(form.getFromMemberId());
        assessmentService.createAssessment(toMember,fromMember,form.getContent(),form.getScore());
        alarmService.changeReadState(id);
        return "redirect:/";
    }
}
