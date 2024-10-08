package project.general_project.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.general_project.domain.*;
import project.general_project.service.CommentService;
import project.general_project.service.MemberService;
import project.general_project.service.PostService;
import project.general_project.web.form.comment.CommentAddForm;
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

    private static Post makePostWithEditPostForm(EditPostForm editPostForm) {
        Post post = new Post();
        post.setId(editPostForm.getPostId());
        post.setTitle(editPostForm.getTitle());
        post.setContent(editPostForm.getContent());
        post.setStatus(editPostForm.getStatus());
        post.setLevelStatus(editPostForm.getLevelStatus());
        return post;
    }

    private static void addModelIsWriter(Model model, Member loginMember, Post post) {
        if (loginMember == null || !loginMember.getId().equals( post.getMember().getId())) {
            model.addAttribute("isWriter", false);
        } else {
            model.addAttribute("isWriter", true);
        }
    }

    @ModelAttribute("codes")
    public RecruitmentStatus[] codes() {
        return RecruitmentStatus.values();
    }

    @ModelAttribute("levelTypes")
    public LevelStatus[] levelTypes() {
        return LevelStatus.values();
    }

    @GetMapping("/{id}/post/new")
    public String addPostForm(@ModelAttribute("postForm") PostForm postForm, @Login Member loginMember, @PathVariable("id") Long memberId) {
        if (loginMember == null || !loginMember.getId().equals(memberId)) return "redirect:/login";
        return "post/addForm";
    }

    @PostMapping("/{id}/post/new")
    public String addPost(@Validated @ModelAttribute("postForm") PostForm postForm, BindingResult bindingResult, @PathVariable("id") Long memberId, @Login Member loginMember, Model model) {
        if (bindingResult.hasErrors()) {
            return "post/addForm";
        }
        Member findMember = memberService.findById(memberId);
        if (!loginMember.getId().equals(findMember.getId())) {
            return "redirect:/login";
        }
        Post post = Post.createPost(findMember, postForm.getTitle(), postForm.getContent(), postForm.getLevelStatus());
        postService.save(post);
        return "redirect:/loginHome";
    }

    @GetMapping("/post/{id}")
    public String postForm(@PathVariable("id") Long postId, Model model, @Login Member loginMember, @RequestParam(name = "comment", defaultValue = "-1") Long commentId, @RequestParam(name = "page", defaultValue = "1") Integer page) {
        Optional<Post> findPost = postService.findByIdWithMember(postId);
        if (!findPost.isPresent()) return "redirect:/";
        Post post = findPost.get();
        PostForm postForm = new PostForm(post.getTitle(), post.getContent(), post.getStatus(), post.getId(), post.getMember().getUsername(), post.getCreated(), post.getLevelStatus());
        model.addAttribute("postForm", postForm);
        addModelIsWriter(model, loginMember, post);
        addCommentToModel(model, post.getId(), page);
        addCommentCount(model, post.getId());
        model.addAttribute("commentForm", new CommentAddForm());
        model.addAttribute("id", commentId);
        if (commentId != -1) {
            List<Comment> childComment = commentService.findCommentByParentId(commentId);
            model.addAttribute("children", childComment);
        }
        return "post/postForm";
    }

    private void addCommentToModel(Model model, Long postId, Integer page) {
        List<Comment> findComments = commentService.findCommentByPost(postId, page - 1, 5);
        List<CommentForm> comments = findComments.stream().map(o -> {
            CommentForm commentForm = new CommentForm();
            commentForm.setUsername(o.getMember().getUsername());
            commentForm.setContent(o.getContent());
            commentForm.setCreated(o.getCreated());
            commentForm.setId(o.getId());
            return commentForm;
        }).collect(Collectors.toList());
        model.addAttribute("comments", comments);
        model.addAttribute("currentPage", page);
    }

    private void addCommentCount(Model model, Long postId) {
        Long mainCommentCount = commentService.getMainCommentCount(postId);
        Long count = mainCommentCount / 5;
        if (mainCommentCount % 5 != 0) count++;
        if (count == 0) count = 1L;
        model.addAttribute("totalPages", count);

    }

    @GetMapping("/post/{postId}/comments/new")
    public String returnToPost(@PathVariable("postId") Long postId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("postId", postId);
        return "redirect:/post/{postId}";
    }

    @PostMapping("/post/{postId}/comments/new")
    public String addComment(@Validated @ModelAttribute("commentForm") CommentAddForm form, BindingResult bindingResult, @PathVariable("postId") Long postId, Model model, @Login Member loginMember, RedirectAttributes redirectAttributes, @ModelAttribute PostForm postForm) {

        redirectAttributes.addAttribute("postId", postId);
        if (loginMember == null) {
            return "redirect:/login?redirectURI=/post/{postId}";
        }
        if (bindingResult.hasErrors()) {
            addCommentToModel(model, postId, 1);
            return "post/postForm";
        }
        Post post = postService.findByIdWithMember(postId).get();
        Comment comment = Comment.createComment(loginMember, form.getContent(), post);
        commentService.saveInPost(comment);
        return "redirect:/post/{postId}";
    }

    @GetMapping("/post/{postId}/comments/{commentId}/new")
    public String returnToPostWithChild(@PathVariable Long postId, @PathVariable Long commentId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("postId", postId);
        redirectAttributes.addAttribute("commentId", commentId);
        return "redirect:/post/{postId}?comment={commentId}";
    }

    @PostMapping("/post/{postId}/comments/{commentId}/new")
    public String addChildComment(@Validated @ModelAttribute("commentForm") CommentAddForm form, BindingResult bindingResult, @PathVariable Long postId, @PathVariable Long commentId, Model model, @Login Member loginMember, RedirectAttributes redirectAttributes, @ModelAttribute PostForm postForm) {

        redirectAttributes.addAttribute("postId", postId);
        redirectAttributes.addAttribute("commentId", commentId);
        if (loginMember == null) {
            return "redirect:/login?redirectURI=/post/{postId}/comments/{postId}";
        }
        if (bindingResult.hasErrors()) {
            addCommentToModel(model, postId, 1);
            return "post/postForm";
        }
        Post post = postService.findByIdWithMember(postId).get();
        Comment comment = Comment.createComment(loginMember, form.getContent(), post);
        commentService.saveInComment(comment, commentId);
        return "redirect:/post/{postId}?comment={commentId}";
    }

    @GetMapping("/post/{postId}/edit")
    public String editPostForm(@PathVariable("postId") Long postId, @Login Member loginMember, Model model) {
        Optional<Post> findPost = postService.findByIdWithMember(postId);
        if (findPost.isEmpty()) return "redirect:/";
        Post post = findPost.get();
        if (!loginMember.getId().equals(post.getMember().getId())) return "redirect:/";
        EditPostForm editPostForm = new EditPostForm(post.getTitle(), post.getContent(), post.getStatus(), post.getId(), post.getLevelStatus());
        model.addAttribute("editPostForm", editPostForm);
        return "post/editForm";
    }

    @PostMapping("/post/{postId}/edit")
    public String editPost(@Validated @ModelAttribute("editPostForm") EditPostForm editPostForm, BindingResult bindingResult, @Login Member loginMember, RedirectAttributes redirectAttributes) {
        Post findPost = postService.findByIdWithMember(editPostForm.getPostId()).get();
        if (findPost == null || !findPost.getMember().getId().equals(loginMember.getId())) return "redirect:/";
        if (bindingResult.hasErrors()) {
            return "post/editForm";
        }
        log.info("level={}", editPostForm.getLevelStatus());
        Post post = makePostWithEditPostForm(editPostForm);
        Long id = postService.updatePost(post);
        redirectAttributes.addAttribute("id", id);
        return "redirect:/post/{id}";
    }

    @PostMapping("/post/{postId}/delete")
    public String deletePost(@Login Member member, @ModelAttribute("postForm") PostForm postForm, @PathVariable("postId") Long postId) {
        Optional<Post> findPost = postService.findByIdWithMember(postId);
        if (findPost.isEmpty()) return "redirect:/";
        if (!findPost.get().getMember().getId().equals(member.getId())) return "redirect:/";
        postService.deletePost(postId);
        return "redirect:/";
    }

}
