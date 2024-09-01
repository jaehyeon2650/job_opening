package project.general_project.repository.post;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.general_project.domain.Comment;
import project.general_project.domain.Post;
import project.general_project.repository.comment.CommentRepository;

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

    @Test
    public void 포스트_삭제() throws Exception{
        //given
        Post post=new Post();
        postRepository.save(post);
        Comment comment1=new Comment();
        comment1.setPost(post);
        Comment comment2=new Comment();
        comment2.setPost(post);
        Comment comment3=new Comment();
        comment3.setPost(post);
        Comment comment4=new Comment();
        comment4.setPost(post);
        comment1.addComment(comment2);
        comment3.addComment(comment4);
        em.persist(comment1);
        em.persist(comment3);
        //when
        postRepository.deletePost(post.getId());
        //then
        Comment comment1Find = em.find(Comment.class, comment1.getId());
        Comment comment2Find = em.find(Comment.class, comment2.getId());
        Comment comment3Find = em.find(Comment.class, comment3.getId());
        Comment comment4Find = em.find(Comment.class, comment4.getId());
        assertThat(comment1Find).isNull();
        assertThat(comment2Find).isNull();
        assertThat(comment3Find).isNull();
        assertThat(comment4Find).isNull();
        Post findPost = em.find(Post.class, post.getId());
        assertThat(findPost).isNull();
    }
}