package project.general_project.repository.post;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.general_project.domain.Comment;
import project.general_project.domain.Post;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
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

    @Test
    public void 포스트_찾기() throws Exception{
        //given
        Post post1=new Post();
        post1.setTitle("aabbcc");
        post1.setContent("aaccbbbb");
        postRepository.save(post1);
        Post post2=new Post();
        post2.setTitle("spring");
        post2.setContent("spring123123");
        postRepository.save(post2);
        //when
        List<Post> findPosts = postRepository.getPostsByTitle("spri",0,10);

        //then
        assertThat(findPosts.size()).isEqualTo(1);
    }
    @Test
    public void 포스트_찾기_대소문자구별() throws Exception{
        //given
        Post post1=new Post();
        post1.setTitle("aabbcc");
        post1.setContent("aaccbbbb");
        postRepository.save(post1);
        Post post2=new Post();
        post2.setTitle("Spring");
        post2.setContent("spring123123");
        postRepository.save(post2);
        //when
        List<Post> findPosts = postRepository.getPostsByTitle("spri",0,10);
        //then
        assertThat(findPosts.size()).isEqualTo(1);
    }
    @Test
    public void 포스트_찾기_대소문자구별2() throws Exception{
        //given
        Post post1=new Post();
        post1.setTitle("aabbcc");
        post1.setContent("aaccbbbb");
        postRepository.save(post1);
        Post post2=new Post();
        post2.setTitle("Spring");
        post2.setContent("spring123123");
        postRepository.save(post2);
        //when
        List<Post> findPosts = postRepository.getPostsByTitle("SPRI",0,10);
        //then
        assertThat(findPosts.size()).isEqualTo(1);
    }
    @Test
    public void 포스트_찾기_대소문자구별3() throws Exception{
        //given
        Post post1=new Post();
        post1.setTitle("aabbcc");
        post1.setContent("aaccbbbb");
        postRepository.save(post1);
        Post post2=new Post();
        post2.setTitle("Spring");
        post2.setContent("spring123123");
        postRepository.save(post2);
        for(int i=0;i<40;i++){
            Post post=new Post();
            post.setTitle("asdas"+i);
            postRepository.save(post);
        }
        //when
        List<Post> findPosts = postRepository.getPostsByTitle("Da",0,10);
        //then
        assertThat(findPosts.size()).isEqualTo(10);
    }
    @Test
    public void 빈_문자열로_모든_포스트_찾기() throws Exception{
        //given
        Post post1=new Post();
        post1.setTitle("aabbcc");
        post1.setContent("aaccbbbb");
        postRepository.save(post1);
        Post post2=new Post();
        post2.setTitle("Spring");
        post2.setContent("spring123123");
        postRepository.save(post2);
        for(int i=0;i<40;i++){
            Post post=new Post();
            post.setTitle("asdas"+i);
            postRepository.save(post);
        }
        //when
        List<Post> findPosts = postRepository.getPostsByTitle("",0,10);
        //then
        assertThat(findPosts.size()).isEqualTo(10);
    }
    @Test
    public void 널로_모든_포스트_찾기() throws Exception{
        //given
        Post post1=new Post();
        post1.setTitle("aabbcc");
        post1.setContent("aaccbbbb");
        postRepository.save(post1);
        Post post2=new Post();
        post2.setTitle("Spring");
        post2.setContent("spring123123");
        postRepository.save(post2);
        for(int i=0;i<40;i++){
            Post post=new Post();
            post.setTitle("asdas"+i);
            postRepository.save(post);
        }
        //when
        List<Post> findPosts = postRepository.getPostsByTitle(null,0,10);
        //then
        assertThat(findPosts.size()).isEqualTo(10);
    }
    @Test
    public void 총_포스트_개수() throws Exception{
        //given
        Post post1=new Post();
        post1.setTitle("aabbcc");
        post1.setContent("aaccbbbb");
        postRepository.save(post1);
        Post post2=new Post();
        post2.setTitle("Spring");
        post2.setContent("spring123123");
        postRepository.save(post2);
        for(int i=0;i<40;i++){
            Post post=new Post();
            post.setTitle("asdas"+i);
            postRepository.save(post);
        }
        //when
        Long postCount = postRepository.getPostCount();
        //then
        assertThat(postCount).isEqualTo(42);
    }
}