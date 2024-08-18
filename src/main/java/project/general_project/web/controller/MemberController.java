package project.general_project.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import project.general_project.domain.Address;
import project.general_project.domain.Member;
import project.general_project.service.LoginService;
import project.general_project.service.MemberService;
import project.general_project.validation.JoinValidator;
import project.general_project.web.SessionConst;
import project.general_project.web.join.JoinForm;
import project.general_project.web.login.Login;
import project.general_project.web.login.LoginForm;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final LoginService loginService;
    private final JoinValidator joinValidator;

    @InitBinder("joinForm")
    public void init(WebDataBinder dataBinder){
        dataBinder.addValidators(joinValidator);
    }

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm loginForm, BindingResult bindingResult) {
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
        Member member=Member.createMember(joinForm.getUsername(),joinForm.getUserId(),joinForm.getPassword(),address);
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

}
