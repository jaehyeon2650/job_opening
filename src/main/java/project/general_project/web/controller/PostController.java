package project.general_project.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.general_project.domain.Comment;
import project.general_project.domain.Member;
import project.general_project.domain.Post;
import project.general_project.domain.RecruitmentStatus;
import project.general_project.service.CommentService;
import project.general_project.service.MemberService;
import project.general_project.service.PostService;
import project.general_project.web.form.comment.CommentForm;
import project.general_project.web.form.memberForm.Login;
import project.general_project.web.form.postForm.EditPostForm;
import project.general_project.web.form.postForm.PostForm;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final MemberService memberService;
    private final CommentService commentService;

    @ModelAttribute("codes")
    public RecruitmentStatus[] codes(){
        return RecruitmentStatus.values();
    }

    @GetMapping("/{id}/post/new")
    public String addPostForm(@ModelAttribute("postForm")PostForm postForm,@Login Member loginMember,@PathVariable("id") Long memberId){
        if(loginMember==null||loginMember.getId()!=memberId) return "redirect:/login";
        return "addForm";
    }

    @PostMapping("/{id}/post/new")
    public String addPost(@Validated @ModelAttribute("postForm") PostForm postForm, BindingResult bindingResult, @PathVariable("id") Long memberId, @Login Member loginMember, Model model){
        if(bindingResult.hasErrors()){
            return "addForm";
        }
        Member findMember = memberService.findById(memberId);
        if(loginMember.getId() != findMember.getId()){
            return "redirect:/login";
        }
        Post post = Post.createPost(findMember, postForm.getTitle(), postForm.getContent());
        postService.save(post);
        model.addAttribute("member",findMember);
        addPostFormToModel(model);
        return "loginHome";
    }

    @GetMapping("/post/{id}")
    public String postForm(@PathVariable("id") Long postId, Model model, @Login Member loginMember, @RequestParam(name = "comment",defaultValue = "-1") Long commentId){
        Optional<Post> findPost = postService.findByIdWithMember(postId);
        if(!findPost.isPresent()) return "redirect:/";
        Post post = findPost.get();
        PostForm postForm=new PostForm(post.getTitle(),post.getContent(),post.getStatus(),post.getId(),post.getMember().getUsername(),post.getCreated());
        model.addAttribute("postForm",postForm);
        addModelIsWriter(model, loginMember, post);
        List<Comment> comments = commentService.findCommentByPost(post.getId(), 0, 10);
        model.addAttribute("comments",comments);
        model.addAttribute("commentForm",new CommentForm());
        model.addAttribute("id",commentId);
        if(commentId!=-1){
            List<Comment> childComment = commentService.findCommentByParentId(commentId);
            model.addAttribute("children",childComment);
        }
        return "postForm";
    }

    @GetMapping("/post/{postId}/comments/new")
    public String returnToPost(@PathVariable("postId") Long postId,RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("postId",postId);
        return "redirect:/post/{postId}";
    }
    @PostMapping("/post/{postId}/comments/new")
    public String addComment(@Validated @ModelAttribute("commentForm") CommentForm form,BindingResult bindingResult, @PathVariable("postId") Long postId, Model model, @Login Member loginMember,RedirectAttributes redirectAttributes,@ModelAttribute PostForm postForm){

        redirectAttributes.addAttribute("postId",postId);
        if(loginMember==null){
            return "redirect:/login?redirectURI=/post/{postId}";
        }
        if(bindingResult.hasErrors()){
            List<Comment> comments = commentService.findCommentByPost(postId, 0, 10);
            model.addAttribute("comments",comments);
            return "postForm";
        }
        Post post = postService.findByIdWithMember(postId).get();
        Comment comment=Comment.createComment(loginMember,form.getContent(),post);
        commentService.saveInPost(comment);
        return "redirect:/post/{postId}";
    }
    @GetMapping("/post/{postId}/comments/{commentId}/new")
    public String returnToPostWithChild(@PathVariable Long postId,@PathVariable Long commentId,RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("postId",postId);
        redirectAttributes.addAttribute("commentId",commentId);
        return "redirect:/post/{postId}?comment={commentId}";
    }
    @PostMapping("/post/{postId}/comments/{commentId}/new")
    public String addChildComment(@Validated @ModelAttribute("commentForm") CommentForm form,BindingResult bindingResult, @PathVariable Long postId,@PathVariable Long commentId, Model model, @Login Member loginMember,RedirectAttributes redirectAttributes,@ModelAttribute PostForm postForm){

        redirectAttributes.addAttribute("postId",postId);
        redirectAttributes.addAttribute("commentId",commentId);
        if(loginMember==null){
            return "redirect:/login?redirectURI=/post/{postId}/comments/{postId}";
        }
        if(bindingResult.hasErrors()){
            List<Comment> comments = commentService.findCommentByPost(postId, 0, 10);
            model.addAttribute("comments",comments);
            return "postForm";
        }
        Post post = postService.findByIdWithMember(postId).get();
        Comment comment=Comment.createComment(loginMember,form.getContent(),post);
        commentService.saveInComment(comment,commentId);
        return "redirect:/post/{postId}?comment={commentId}";
    }

    @GetMapping("/post/{postId}/edit")
    public String editPostForm(@PathVariable("postId") Long postId,@Login Member loginMember,Model model){
        Optional<Post> findPost = postService.findByIdWithMember(postId);
        if(findPost.isEmpty()) return "redirect:/";
        Post post = findPost.get();
        if(loginMember.getId()!=post.getMember().getId()) return "redirect:/";
        EditPostForm editPostForm=new EditPostForm(post.getTitle(),post.getContent(),post.getStatus(),post.getId());
        model.addAttribute("editPostForm",editPostForm);
        return "editForm";
    }

    @PostMapping("/post/{postId}/edit")
    public String editPost(@Validated @ModelAttribute("editPostForm") EditPostForm editPostForm, BindingResult bindingResult, @Login Member loginMember, RedirectAttributes redirectAttributes){
        Post findPost = postService.findByIdWithMember(editPostForm.getPostId()).get();
        if(findPost==null||findPost.getMember().getId()!=loginMember.getId()) return "redirect:/";
        if(bindingResult.hasErrors()){
            return "editForm";
        }
        Post post = makePostWithEditPostForm(editPostForm);
        Long id = postService.updatePost(post);
        redirectAttributes.addAttribute("id",id);
        return "redirect:/post/{id}";
    }


    private static Post makePostWithEditPostForm(EditPostForm editPostForm) {
        Post post=new Post();
        post.setId(editPostForm.getPostId());
        post.setTitle(editPostForm.getTitle());
        post.setContent(editPostForm.getContent());
        post.setStatus(editPostForm.getStatus());
        return post;
    }

    private static void addModelIsWriter(Model model, Member loginMember, Post post) {
        if(loginMember ==null|| loginMember.getId()!= post.getMember().getId()){
            model.addAttribute("isWriter",false);
        }else{
            model.addAttribute("isWriter",true);
        }
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
