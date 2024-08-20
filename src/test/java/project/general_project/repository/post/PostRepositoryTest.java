package project.general_project.repository.post;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.general_project.domain.Comment;
import project.general_project.domain.Post;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
class PostRepositoryTest {
    @Autowired
    EntityManager em;
    @Autowired
    PostRepository postRepository;

    @Test
    public void 포스트_저징() throws Exception{
        //given
        Post post=new Post();
        post.setContent("asdasd");
        post.setTitle("asdasasdd");

        //when
        postRepository.save(post);
        Post findPost = postRepository.findById(post.getId());
        //then
        assertThat(findPost).isEqualTo(post);
    }

    @Test
    public void 댓글_저장() throws Exception{
        //given
        Post post1=new Post();
        Post post2=new Post();
        Comment comment1=new Comment();
        post1.addComment(comment1);
        postRepository.save(post1);
        postRepository.save(post2);
        //when
        Comment findComment = postRepository.findCommentById(comment1.getCommentId());
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
        post1.addComment(comment1);
        post1.addComment(comment2);
        postRepository.save(post1);
        postRepository.save(post2);
        //when
        Post post1Find = postRepository.findById(post1.getId());
        Post post2Find = postRepository.findById(post2.getId());
        //then
        assertThat(post1Find.getComments().size()).isEqualTo(2);
        assertThat(post2Find.getComments().size()).isEqualTo(0);
    }

    @Test
    public void 페이징() throws Exception{
        //given
        for(int i=0;i<100;i++){
            Post post=new Post();
            postRepository.save(post);
        }
        //when
        List<Post> posts = postRepository.getPosts(1, 10);
        //then
        assertThat(posts.size()).isEqualTo(10);
    }

    @Test
    public void 페이징2() throws Exception{
        //given
        for(int i=0;i<8;i++){
            Post post=new Post();
            postRepository.save(post);
        }
        //when
        List<Post> posts = postRepository.getPosts(0, 10);
        //then
        assertThat(posts.size()).isEqualTo(8);
    }
}