package project.general_project.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.general_project.domain.Comment;
import project.general_project.domain.Member;
import project.general_project.domain.Post;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentServiceTest {
    @Autowired
    CommentService service;
    @Autowired
    PostService postService;
    @Autowired
    MemberService memberService;

    @Test
    public void 포스트에_댓글_저장() throws Exception{
        //given
        Post post=new Post();
        postService.save(post);
        Comment comment1=Comment.createComment(null,"adsd",post);
        Comment comment2=Comment.createComment(null,"adsd",post);
        Comment comment3=Comment.createComment(null,"adsd",post);

        Member member=new Member();
        member.setPassword("asda");
        memberService.save(member);

        comment1.setMember(member);
        comment2.setMember(member);
        comment3.setMember(member);


        service.saveInPost(comment1);
        service.saveInPost(comment2);
        service.saveInPost(comment3);
        //when
        List<Comment> commentByPost = service.findCommentByPost(post.getId(),0,10);

        //then
        assertThat(commentByPost.size()).isEqualTo(3);
    }

    @Test
    public void 댓글에_부속댓글_달기() throws Exception{
        //given
        Comment comment1=new Comment();
        Comment comment2=new Comment();
        Comment comment3=new Comment();
        Comment comment4=new Comment();

        Member member=new Member();
        member.setPassword("asdsa");
        memberService.save(member);

        comment1.setMember(member);
        comment2.setMember(member);
        comment3.setMember(member);
        comment4.setMember(member);

        service.saveInPost(comment1);
        service.saveInPost(comment2);

        service.saveInComment(comment3,comment1.getId());
        service.saveInComment(comment4,comment1.getId());
        //when
        List<Comment> commentByParentId = service.findCommentByParentId(comment1.getId());
        //then
        assertThat(commentByParentId.size()).isEqualTo(2);
    }

    @Test
    public void 댓글에_부속댓글_달기2() throws Exception{
        //given
        Comment comment1=new Comment();
        Comment comment2=new Comment();
        Comment comment3=new Comment();
        Comment comment4=new Comment();

        service.saveInPost(comment1);
        service.saveInPost(comment2);

        service.saveInComment(comment3,comment1.getId());
        service.saveInComment(comment4,comment1.getId());
        //when
        List<Comment> commentByParentId = service.findCommentByParentId(comment2.getId());
        //then
        assertThat(commentByParentId.size()).isEqualTo(0);
    }
}

