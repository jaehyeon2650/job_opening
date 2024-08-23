package project.general_project.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import project.general_project.domain.Member;
import project.general_project.domain.Post;
import project.general_project.service.PostService;
import project.general_project.web.SessionConst;
import project.general_project.web.form.memberForm.Login;
import project.general_project.web.form.postForm.PostForm;
import project.general_project.web.form.postForm.PostSearchForm;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
    private final PostService postService;

    @GetMapping("/")
    public String home(@Login Member member, Model model, @ModelAttribute("postSearchForm") PostSearchForm postSearchForm, @RequestParam(name = "page",defaultValue = "1") Integer page){
        addPostFormToModel(model,postSearchForm,page);
        addPostCount(model);

        if(member==null){
            return "home";
        }
        model.addAttribute("member",member);
        return "loginHome";
    }


    @GetMapping("/loginHome")
    public String loginHome(@Login Member member,Model model,@ModelAttribute("postSearchForm") PostSearchForm postSearchForm,@RequestParam(name = "page",defaultValue = "1") Integer page){
        addPostFormToModel(model,postSearchForm,page);
        addPostCount(model);

        if(member==null){
            return "redirect:/";
        }
        model.addAttribute("member",member);
        return "loginHome";
    }

    private void addPostFormToModel(Model model,PostSearchForm postSearchForm,Integer page) {
        List<Post> posts = postService.findPostsByTitle(postSearchForm.getContent(),page-1,10);
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
        model.addAttribute("postSearchForm",new PostSearchForm());
        model.addAttribute("currentPage",page);
    }

    private void addPostCount(Model model){
        Long postCount = postService.getPostCount();
        Long count=postCount/10;
        if(postCount%10>0) count++;
        if(count==0) count++;
        model.addAttribute("totalPages",count);
    }
}
