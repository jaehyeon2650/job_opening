package project.general_project.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.general_project.domain.Comment;
import project.general_project.domain.Post;
import project.general_project.repository.post.PostRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class PostServiceTest {
    @Autowired
    EntityManager em;
    @Autowired
    PostRepository repository;
    @Autowired
    PostService service;

    @Test
    public void 포스트_저장() throws Exception{
        //given
        Post post=new Post();

        //when
        service.save(post);
        Optional<Post> findPost = service.findById(post.getId());
        //then
        assertThat(findPost.get()).isEqualTo(post);
    }

    @Test
    public void 댓글_저장() throws Exception{
        //given
        Post post1=new Post();
        Post post2=new Post();
        Comment comment1=new Comment();

        service.save(post1);
        service.save(post2);
        //when
        service.saveComment(post1.getId(),comment1);
        Comment findComment = repository.findCommentById(comment1.getId());
        //then
        assertThat(findComment).isEqualTo(comment1);
        assertThat(findComment.getPost()).isEqualTo(post1);
    }

    @Test
    public void 댓글_저장_2() throws Exception{
        //given
        Post post1=new Post();
        Post post2=new Post();
        Comment comment1=new Comment();
        Comment comment2=new Comment();

        service.save(post1);
        service.save(post2);
        //when
        service.saveComment(post1.getId(),comment1);
        service.saveComment(post1.getId(),comment2);
        Post findPost1 = service.findById(post1.getId()).get();
        Post findPost2 = service.findById(post2.getId()).get();
        //then
        assertThat(findPost1.getComments().size()).isEqualTo(2);
        assertThat(findPost2.getComments().size()).isEqualTo(0);
    }
}