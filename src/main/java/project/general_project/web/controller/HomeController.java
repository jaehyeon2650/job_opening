package project.general_project.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import project.general_project.domain.Member;
import project.general_project.web.SessionConst;

@Controller
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/")
    public String home(@SessionAttribute(name = SessionConst.LOGIN_MEMBER,required = false) Member member,Model model){
        if(member==null){
            return "home";
        }
        model.addAttribute("member",member);
        return "loginHome";
    }

    @GetMapping("/loginHome")
    public String loginHome(@SessionAttribute(name = SessionConst.LOGIN_MEMBER,required = false) Member member,Model model){
        if(member==null){
            return "redirect:/";
        }
        model.addAttribute("member",member);
        return "loginHome";
    }
}
