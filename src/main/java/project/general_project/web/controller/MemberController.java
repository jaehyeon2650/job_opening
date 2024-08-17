package project.general_project.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import project.general_project.domain.Address;
import project.general_project.domain.Member;
import project.general_project.service.MemberService;
import project.general_project.web.join.JoinForm;
import project.general_project.web.login.LoginForm;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    @GetMapping("/")
    public String home(){
        return "home";
    }
    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm loginForm) {
        return "login";
    }

    @GetMapping("/join")
    public String joinForm(@ModelAttribute("joinForm") JoinForm joinForm) {
        return "join";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute("joinForm") JoinForm joinForm){
        log.info("login");
        Address address=Address.createAddress(joinForm.getZipcode(),joinForm.getCity(),joinForm.getDetailAddress());
        Member member=Member.createMember(joinForm.getUsername(),joinForm.getUserId(),joinForm.getPassword(),address);
        memberService.save(member);
        return "redirect:/";
    }


}
