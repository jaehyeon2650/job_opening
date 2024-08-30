package project.general_project.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import project.general_project.domain.Address;
import project.general_project.domain.Member;
import project.general_project.domain.Post;
import project.general_project.service.LoginService;
import project.general_project.service.MemberService;
import project.general_project.service.PostService;
import project.general_project.validation.EditValidator;
import project.general_project.validation.JoinValidator;

import project.general_project.web.SessionConst;
import project.general_project.web.form.memberForm.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final LoginService loginService;
    private final PostService postService;
    private final JoinValidator joinValidator;
    private final EditValidator editValidator;

    @InitBinder("joinForm")
    public void init(WebDataBinder dataBinder){
        dataBinder.addValidators(joinValidator);
    }

    @InitBinder("editForm")
    public void init2(WebDataBinder dataBinder){
        dataBinder.addValidators(editValidator);
    }
    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm loginForm, BindingResult bindingResult, @Login Member member) {
        if (member != null) {
            return "redirect:/loginHome";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute("loginForm") LoginForm loginForm, BindingResult bindingResult, HttpServletRequest request, @RequestParam(defaultValue = "/") String redirectURI){

        if(bindingResult.hasErrors()){
            return "login";
        }

        Member loginMember = loginService.login(loginForm.getId(), loginForm.getPassword());
        if(loginMember==null){
            bindingResult.reject("loginFail","아이디 혹은 비밀번호가 잘못되었습니다.");
            return "login";
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER,loginMember);
        return "redirect:"+redirectURI;

    }

    @GetMapping("/join")
    public String joinForm(@ModelAttribute("joinForm") JoinForm joinForm) {
        return "join";
    }

    @PostMapping("/join")
    public String join(@Validated @ModelAttribute("joinForm") JoinForm joinForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "join";
        }
        Address address=Address.createAddress(joinForm.getZipcode(),joinForm.getCity(),joinForm.getDetailAddress());
        String number= joinForm.getFirstPhone()+joinForm.getSecondPhone()+joinForm.getThirdPhone();
        Member member=Member.createMember(joinForm.getUsername(),joinForm.getUserId(),joinForm.getPassword(),number,joinForm.getEmail(),address);
        Long save = memberService.save(member);
        if(save==-1){
            bindingResult.reject("duplicateId");
            return "join";
        }
        return "redirect:/";
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session!=null){
            session.invalidate();
        }
        return "redirect:/";
    }

    @GetMapping("/member/{id}/edit")
    public String memberEditForm(@PathVariable("id") Long memerId, Model model){
        Member findMember = memberService.findById(memerId);
        EditForm editForm=new EditForm(findMember);
        model.addAttribute("editForm",editForm);
        return "updateMemberForm";
    }

    @PostMapping("/member/{id}/edit")
    public String editMember(@Validated @ModelAttribute("editForm") EditForm editForm,BindingResult bindingResult,@PathVariable("id") Long memerId,Model model){
        if(bindingResult.hasErrors()){
            log.info("error");
            return "updateMemberForm";
        }
        Address address=Address.createAddress(editForm.getZipcode(),editForm.getCity(),editForm.getDetailAddress());
        String number= editForm.getFirstPhone()+editForm.getSecondPhone()+editForm.getThirdPhone();
        Member member=Member.createMember(editForm.getId(),editForm.getUsername(),number,editForm.getEmail(),address);
        Long memberId = memberService.updateMember(member);
        Member findMember = memberService.findById(memberId);
        model.addAttribute("member",findMember);
        return "redirect:/loginHome";
    }

}
