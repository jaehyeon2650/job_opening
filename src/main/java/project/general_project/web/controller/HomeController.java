package project.general_project.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import project.general_project.domain.LevelStatus;
import project.general_project.domain.Member;
import project.general_project.domain.Post;
import project.general_project.service.MemberService;
import project.general_project.service.PostService;
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
    private final MemberService memberService;

    private static List<PostForm> makeForms(List<Post> posts) {
        List<PostForm> postForms = posts.stream().map(o -> {
            String content = "";
            if (o.getContent().length() > 100) {
                content = o.getContent().substring(0, 100);
            } else {
                content = o.getContent();
            }
            return new PostForm(o.getTitle(), content, o.getStatus(), o.getId(), o.getLevelStatus());
        }).collect(Collectors.toList());
        return postForms;
    }

    @GetMapping("/")
    public String home(@Login Member member, Model model, @ModelAttribute("postSearchForm") PostSearchForm postSearchForm, @RequestParam(name = "page", defaultValue = "1") Integer page, @RequestParam(name = "level", required = false) String level) {
        addPostFormToModel(model, postSearchForm, page, level);
        addPostCount(model, level);

        if (member == null) {
            return "home";
        }
        Member findMember = memberService.findByIdWithTeam(member.getId());
        model.addAttribute("member", findMember);
        return "loginHome";
    }

    @GetMapping("/loginHome")
    public String loginHome(@Login Member member, Model model, @ModelAttribute("postSearchForm") PostSearchForm postSearchForm, @RequestParam(name = "page", defaultValue = "1") Integer page, @RequestParam(name = "level", required = false) String level) {
        addPostFormToModel(model, postSearchForm, page, level);
        addPostCount(model, level);

        if (member == null) {
            return "redirect:/";
        }
        Member findMember = memberService.findByIdWithTeam(member.getId());
        model.addAttribute("member", findMember);
        return "loginHome";
    }

    private void addPostFormToModel(Model model, PostSearchForm postSearchForm, Integer page, String level) {
        List<Post> posts = findPostsByLevelStatus(postSearchForm, page, level);
        List<PostForm> postForms = makeForms(posts);
        model.addAttribute("postForms", postForms);
        model.addAttribute("postSearchForm", new PostSearchForm());
        model.addAttribute("currentPage", page);
        model.addAttribute("level", level);
    }

    private List<Post> findPostsByLevelStatus(PostSearchForm postSearchForm, Integer page, String level) {
        List<Post> posts;
        if (StringUtils.hasText(level)) {
            if (level.equals(LevelStatus.BEGINNER.getDescription())) {
                posts = postService.findPostsByLevelStatus(LevelStatus.BEGINNER, page - 1, 10);
            } else if (level.equals(LevelStatus.INTERMEDIATE.getDescription())) {
                posts = postService.findPostsByLevelStatus(LevelStatus.INTERMEDIATE, page - 1, 10);
            } else {
                posts = postService.findPostsByLevelStatus(LevelStatus.ADVANCED, page - 1, 10);
            }
        } else {
            posts = postService.findPostsByTitle(postSearchForm.getContent(), page - 1, 10);
        }
        return posts;
    }

    private void addPostCount(Model model, String level) {
        Long postCount = getPostCountByLevel(level);
        Long count = postCount / 10;
        if (postCount % 10 > 0) count++;
        if (count == 0) count++;
        model.addAttribute("totalPages", count);
    }

    private Long getPostCountByLevel(String level) {
        Long postCount;
        if (StringUtils.hasText(level)) {
            if (level.equals(LevelStatus.BEGINNER.getDescription())) {
                postCount = postService.getPostCountByLevelStatus(LevelStatus.BEGINNER);
            } else if (level.equals(LevelStatus.INTERMEDIATE.getDescription())) {
                postCount = postService.getPostCountByLevelStatus(LevelStatus.INTERMEDIATE);
            } else {
                postCount = postService.getPostCountByLevelStatus(LevelStatus.ADVANCED);
            }
        } else {
            postCount = postService.getPostCount();
        }
        return postCount;
    }
}
