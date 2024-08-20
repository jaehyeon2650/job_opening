package project.general_project.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import project.general_project.domain.Member;
import project.general_project.domain.Post;
import project.general_project.service.PostService;
import project.general_project.web.SessionConst;
import project.general_project.web.postForm.PostForm;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final PostService postService;

    @GetMapping("/")
    public String home(@SessionAttribute(name = SessionConst.LOGIN_MEMBER,required = false) Member member,Model model){
        addPostFormToModel(model);
        if(member==null){
            return "home";
        }
        model.addAttribute("member",member);
        return "loginHome";
    }


    @GetMapping("/loginHome")
    public String loginHome(@SessionAttribute(name = SessionConst.LOGIN_MEMBER,required = false) Member member,Model model){
        addPostFormToModel(model);
        if(member==null){
            return "redirect:/";
        }
        model.addAttribute("member",member);
        return "loginHome";
    }

    private void addPostFormToModel(Model model) {
        List<Post> posts = postService.findPosts(0, 10);
        List<PostForm> postForms = posts.stream().map(o -> {
            String content="";
            if(o.getContent().length()>100){
                content=o.getContent().substring(0,100);
            }else{
                content=o.getContent();
            }
            return new PostForm(o.getTitle(),content,o.getStatus(),o.getId());
        }).collect(Collectors.toList());
        model.addAttribute("postForms",postForms);
    }
}
