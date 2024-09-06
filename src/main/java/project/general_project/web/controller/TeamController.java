package project.general_project.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.general_project.domain.Member;
import project.general_project.domain.Team;
import project.general_project.exception.NoTeamException;
import project.general_project.exception.NoUserException;
import project.general_project.exception.UserHasTeamException;
import project.general_project.service.MemberService;
import project.general_project.service.TeamService;
import project.general_project.web.form.teamForm.CreateTeamForm;
import project.general_project.web.form.memberForm.Login;
import project.general_project.web.form.teamForm.EditTeamForm;
import project.general_project.web.form.teamForm.TeamForm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @GetMapping("/team/new")
    public String teamForm(@Login Member loginMember, Model model){
        model.addAttribute("member",loginMember);
        model.addAttribute("teamForm",new CreateTeamForm());
        return "addTeamForm";
    }

    @PostMapping("/team/new")
    public String teamForm(@Login Member loginMember, @ModelAttribute("teamForm") TeamForm teamForm, BindingResult bindingResult,Model model){
        List<String> members=new ArrayList<>(Collections.nCopies(teamForm.getMembers().size(),""));
        Collections.copy(members,teamForm.getMembers());
        model.addAttribute("member",loginMember);
        members.add(loginMember.getUserId());
        try{
            teamService.makeTeam(teamForm.getName(), loginMember.getId(),members);
        }catch(NoUserException e){
            bindingResult.rejectValue("members","user");
            return "addTeamForm";
        }catch(UserHasTeamException e){
            bindingResult.rejectValue("members","team");
            return "addTeamForm";
        }
        return "redirect:/";
    }

    @PostMapping("/member/{memberId}/team/{teamId}/delete")
    public String leaveTeam(@PathVariable Long memberId, @PathVariable Long teamId, @Login Member member, RedirectAttributes redirectAttributes){
        if(member.getId()!=memberId) return "redirect:/";
        try{
            teamService.leaveTheTeam(memberId,teamId);
        }catch (NoTeamException e){
            return "redirect:/";
        }
        redirectAttributes.addAttribute("memberId",memberId);
        return "redirect:/member/{memberId}";
    }
}
