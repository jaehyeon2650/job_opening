package project.general_project.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.general_project.domain.Address;
import project.general_project.domain.Assessment;
import project.general_project.domain.Member;
import project.general_project.domain.Post;
import project.general_project.service.*;
import project.general_project.validation.EditValidator;
import project.general_project.validation.JoinValidator;
import project.general_project.web.SessionConst;
import project.general_project.web.form.assessmentForm.AssessmentForm;
import project.general_project.web.form.memberForm.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final LoginService loginService;
    private final AssessmentService assessmentService;
    private final PictureStore pictureStore;
    private final PostService postService;


    private final JoinValidator joinValidator;
    private final EditValidator editValidator;

    @InitBinder("joinForm")
    public void init(WebDataBinder dataBinder) {
        dataBinder.addValidators(joinValidator);
    }

    @InitBinder("editForm")
    public void init2(WebDataBinder dataBinder) {
        dataBinder.addValidators(editValidator);
    }

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm loginForm, @Login Member member) {
        if (member != null) {
            return "redirect:/loginHome";
        }
        return "member/login";
    }

    @PostMapping("/loginFail")
    public String postloginFail(@ModelAttribute("loginForm") LoginForm loginForm,BindingResult bindingResult, @Login Member member,HttpServletRequest request) {
        log.info("loginFail 돌입");
        if (member != null) {
            return "redirect:/loginHome";
        }
        if (bindingResult.hasErrors()) {
            return "member/login";
        }
        bindingResult.reject("loginFail", (String) request.getAttribute("errorMessage"));
        return "member/login";
    }
//    @PostMapping("/login")
    public String login(@ModelAttribute("loginForm") LoginForm loginForm, BindingResult bindingResult, HttpServletRequest request, @RequestParam(defaultValue = "/") String redirectURI) {

        if (bindingResult.hasErrors()) {
            return "member/login";
        }

        Member loginMember = loginService.login(loginForm.getId(), loginForm.getPassword());
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 혹은 비밀번호가 잘못되었습니다.");
            return "member/login";
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        return "redirect:" + redirectURI;

    }

    @GetMapping("/join")
    public String joinForm(@ModelAttribute("joinForm") JoinForm joinForm) {
        return "member/join";
    }

    @PostMapping("/join")
    public String join(@Validated @ModelAttribute("joinForm") JoinForm joinForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "member/join";
        }
        Address address = Address.createAddress(joinForm.getZipcode(), joinForm.getCity(), joinForm.getDetailAddress());
        String number = joinForm.getFirstPhone() + joinForm.getSecondPhone() + joinForm.getThirdPhone();
        Member member = Member.createMember(joinForm.getUsername(), joinForm.getUserId(), joinForm.getPassword(), number, joinForm.getEmail(), address);
        Long save = memberService.save(member);
        if (save == -1) {
            bindingResult.reject("duplicateId");
            return "member/join";
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    @GetMapping("/member/{id}/edit")
    public String memberEditForm(@PathVariable("id") Long memerId, Model model, @Login Member loginMember) {
        if (loginMember.getId() != memerId) return "redirect:/";
        Member findMember = memberService.findById(memerId);
        EditForm editForm = null;
        if (findMember.getPicture() != null) {
            editForm = new EditForm(findMember, findMember.getPicture().getSaveName());
        } else editForm = new EditForm(findMember, null);
        model.addAttribute("editForm", editForm);
        return "member/updateMemberForm";
    }

    @PostMapping("/member/{id}/edit")
    public String editMember(@Validated @ModelAttribute("editForm") EditForm editForm, BindingResult bindingResult, @PathVariable("id") Long memberId, Model model, @Login Member loginMember, RedirectAttributes redirectAttributes) throws IOException {
        if (loginMember.getId() != memberId) return "redirect:/";

        if (bindingResult.hasErrors()) {
            log.info("error");
            return "member/updateMemberForm";
        }

        Address address = Address.createAddress(editForm.getZipcode(), editForm.getCity(), editForm.getDetailAddress());
        String number = editForm.getFirstPhone() + editForm.getSecondPhone() + editForm.getThirdPhone();
        Member member = Member.createMember(editForm.getId(), editForm.getUsername(), number, editForm.getEmail(), address);
        memberService.updateMember(member);
        if (editForm.getMultipartFile() != null && StringUtils.hasText(editForm.getMultipartFile().getOriginalFilename())) {
            log.info("name={}", editForm.getMultipartFile().getOriginalFilename());
            memberService.updatePicture(memberId, editForm.getMultipartFile());
        }
        redirectAttributes.addAttribute("id", memberId);
        return "redirect:/member/{id}";
    }

    @GetMapping("/member/{id}")
    public String memberForm(@PathVariable("id") Long id, Model model, @Login Member loginMember) {
        Member findMember = memberService.findByIdWithTeam(id);
        List<Post> posts = postService.findByMemberId(id);
        MemberForm memberForm = null;
        double score = assessmentService.getAverage(id);
        if (findMember.getPicture() == null) {
            memberForm = new MemberForm(findMember, posts, null, score);
        } else memberForm = new MemberForm(findMember, posts, findMember.getPicture().getSaveName(), score);
        model.addAttribute("memberForm", memberForm);
        model.addAttribute("loginMember", loginMember);
        return "member/memberForm";
    }

    @ResponseBody
    @GetMapping("/images/{savedFile}")
    public Resource getImage(@PathVariable("savedFile") String savedFile) throws MalformedURLException {
        return new UrlResource("file:" + pictureStore.getFullPath(savedFile));
    }

    @GetMapping("/member/{id}/assessment")
    public String getAssessments(@PathVariable Long id, Model model) {
        List<Assessment> assessments = assessmentService.getAllAssessment(id);
        List<AssessmentForm> forms = assessments.stream().map(o -> {
            return new AssessmentForm(null, o.getContent(), o.getScore(), o.getFromMember().getUserId());
        }).collect(Collectors.toList());

        model.addAttribute("forms", forms);
        double average = assessmentService.getAverage(id);
        model.addAttribute("score", average);
        model.addAttribute("id", id);
        return "assessment/assessmentForm";
    }
}
