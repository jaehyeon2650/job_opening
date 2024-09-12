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
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;
    private final MemberService memberService;
    @GetMapping("/team/new")
    public String teamForm(@Login Member loginMember, Model model){
        model.addAttribute("member",loginMember);
        model.addAttribute("teamForm",new CreateTeamForm());
        return "team/addTeamForm";
    }

    @PostMapping("/team/new")
    public String teamForm(@Login Member loginMember, @Validated @ModelAttribute("teamForm") CreateTeamForm teamForm, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("member",loginMember);
            return "team/addTeamForm";
        }
        List<String> members=deleteBlank(teamForm.getMembers());
        model.addAttribute("member",loginMember);
        members.add(loginMember.getUserId());
        try{
            teamService.makeTeam(teamForm.getName(), loginMember.getId(),members);
        }catch(NoUserException e){
            bindingResult.rejectValue("members","user");
            return "team/addTeamForm";
        }catch(UserHasTeamException e){
            bindingResult.rejectValue("members","team");
            return "team/addTeamForm";
        }
        return "redirect:/";
    }

    @GetMapping("/team/{teamId}")
    public String teamForm(@Login Member member,@PathVariable Long teamId,Model model){
        Member findMember = memberService.findByIdWithTeam(member.getId());
        if(!findMember.getTeam().getId().equals(teamId)){
            return "redirect:/";
        }
        Team team = teamService.findTeamById(teamId);
        TeamForm teamForm=new TeamForm(team);
        model.addAttribute("teamForm",teamForm);
        model.addAttribute("member",member);
        return "team/teamForm";
    }

    @GetMapping("/team/{teamId}/edit")
    public String editTeamForm(@Login Member member,@PathVariable Long teamId,Model model){
        Member findMember = memberService.findByIdWithTeam(member.getId());
        if(!findMember.getTeam().getId().equals(teamId)){
            return "redirect:/";
        }
        Team team = teamService.findTeamById(teamId);
        EditTeamForm editTeamForm=new EditTeamForm(team);
        model.addAttribute("editTeamForm",editTeamForm);
        model.addAttribute("member",member);
        return "team/editTeamForm";
    }

    @PostMapping("/team/{teamId}/edit")
    public String editTeam(@Login Member member, @Validated @ModelAttribute("editTeamForm") EditTeamForm editTeamForm,BindingResult bindingResult,@PathVariable Long teamId,RedirectAttributes redirectAttributes,Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("member",member);
            return "team/editTeamForm";
        }
        if(!member.getUserId().equals(editTeamForm.getLeaderUserId())) return "redirect:/";
        List<String> members = deleteBlank(editTeamForm.getMembers());
        try{
            teamService.updateTeam(teamId,editTeamForm.getTeamName(),members);
        }catch(NoUserException e){
            bindingResult.rejectValue("members","user");
            return "team/editTeamForm";
        }catch(UserHasTeamException e){
            bindingResult.rejectValue("members","team");
            return "team/editTeamForm";
        }
        redirectAttributes.addAttribute("teamId",teamId);
        return "redirect:/team/{teamId}";
    }

    @PostMapping("/member/{memberId}/team/{teamId}/delete")
    public String leaveTeam(@PathVariable Long memberId, @PathVariable Long teamId, @Login Member member, RedirectAttributes redirectAttributes, @RequestParam(defaultValue = "member") String url){
        if(member.getId()!=memberId) return "redirect:/";
        try{
            teamService.leaveTheTeam(memberId,teamId);
        }catch (NoTeamException e){
            return "redirect:/";
        }
        if(url.equals("member")){
            redirectAttributes.addAttribute("memberId",memberId);
            return "redirect:/member/{memberId}";
        }
        return "redirect:/";
    }

    private List<String> deleteBlank(List<String> members){
        List<String> newMembers=new ArrayList<>();
        for (String member : members) {
            if(StringUtils.hasText(member)) newMembers.add(member);
        }
        return newMembers;
    }
}
