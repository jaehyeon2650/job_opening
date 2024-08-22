package project.general_project.repository.comment;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.general_project.domain.Comment;
import project.general_project.domain.Post;
import project.general_project.repository.post.PostRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CommentRepositoryTest {
    @Autowired
    CommentRepository repository;
    @Autowired
    PostRepository postRepository;

    @Test
    public void 댓글_추가() throws Exception{
        //given
        Comment comment1=new Comment();
        Comment comment2=new Comment();
        Comment comment3=new Comment();

        repository.save(comment1);
        repository.save(comment2);
        repository.save(comment3);
        //when
        Comment findComment = repository.findById(comment1.getId());

        //then
        assertThat(findComment).isEqualTo(comment1);
    }

    @Test
    public void 포스터에_달린_댓글_찾기() throws Exception{
        //given
        Comment comment1=new Comment();
        Comment comment2=new Comment();
        Comment comment3=new Comment();
        Comment comment4=new Comment();
        Comment comment5=new Comment();

        Post post1=new Post();
        Post post2=new Post();
        postRepository.save(post1);
        postRepository.save(post2);

        comment1.setPost(post1);
        comment2.setPost(post1);
        comment3.setPost(post1);
        comment4.setPost(post2);
        comment5.setPost(post2);

        repository.save(comment1);
        repository.save(comment2);
        repository.save(comment3);
        repository.save(comment4);
        repository.save(comment5);

        //when
        List<Comment> commentByPost = repository.findCommentByPost(post1.getId(),0,10);

        //then
        assertThat(commentByPost.size()).isEqualTo(3);
    }

    @Test
    public void 포스터에_달린_댓글_찾기2() throws Exception{
        //given
        Comment comment1=new Comment();
        Comment comment2=new Comment();
        Comment comment3=new Comment();
        Comment comment4=new Comment();
        Comment comment5=new Comment();

        Post post1=new Post();
        Post post2=new Post();
        Post post3=new Post();
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        comment1.setPost(post1);
        comment2.setPost(post1);
        comment3.setPost(post1);
        comment4.setPost(post2);
        comment5.setPost(post2);

        repository.save(comment1);
        repository.save(comment2);
        repository.save(comment3);
        repository.save(comment4);
        repository.save(comment5);
        //when
        List<Comment> commentByPost = repository.findCommentByPost(post3.getId(),0,10);

        //then
        assertThat(commentByPost.size()).isEqualTo(0);
    }

    @Test
    public void 포스터에_달린_댓글_찾기3() throws Exception{
        //given
        Comment comment1=new Comment();
        Comment comment2=new Comment();
        Comment comment3=new Comment();
        Comment comment4=new Comment();
        Comment comment5=new Comment();

        Post post1=new Post();
        Post post2=new Post();
        Post post3=new Post();
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        comment1.setPost(post1);
        comment2.setPost(post1);
        comment3.setPost(post2);


        repository.save(comment1);
        repository.save(comment2);
        repository.save(comment3);

        comment3.addComment(comment4);
        comment3.addComment(comment5);

        //when
        List<Comment> commentByPost = repository.findCommentByPost(post1.getId(),0,10);
        List<Comment> commentByParentId = repository.findCommentByParentId(comment3.getId());

        //then
        assertThat(commentByPost.size()).isEqualTo(2);
        assertThat(commentByParentId.size()).isEqualTo(2);
    }

    @Test
    public void 부속_댓글_찾기() throws Exception{
        //given
        Comment comment1=new Comment();
        Comment comment2=new Comment();
        Comment comment3=new Comment();
        Comment comment4=new Comment();
        Comment comment5=new Comment();

        comment1.addComment(comment2);
        comment1.addComment(comment3);
        comment4.addComment(comment5);

        repository.save(comment1);
        repository.save(comment4);
        //when
        List<Comment> commentByParentId = repository.findCommentByParentId(comment1.getId());
        //then
        assertThat(commentByParentId.size()).isEqualTo(2);
    }

    @Test
    public void 부속_댓글_찾기2() throws Exception{
        //given
        Comment comment1=new Comment();
        Comment comment2=new Comment();
        Comment comment3=new Comment();
        Comment comment4=new Comment();
        Comment comment5=new Comment();

        comment1.addComment(comment2);
        comment1.addComment(comment3);
        comment4.addComment(comment5);

        repository.save(comment1);
        repository.save(comment4);
        //when
        List<Comment> commentByParentId1 = repository.findCommentByParentId(comment5.getId());
        List<Comment> commentByParentId2 = repository.findCommentByParentId(comment4.getId());
        //then
        assertThat(commentByParentId1.size()).isEqualTo(0);
        assertThat(commentByParentId2.size()).isEqualTo(1);
    }

    @Test
    public void 댓글_개수() throws Exception{
        //given
        Comment comment1=new Comment();
        Comment comment2=new Comment();
        Comment comment3=new Comment();
        Comment comment4=new Comment();
        Comment comment5=new Comment();

        comment1.addComment(comment2);
        comment1.addComment(comment3);
        comment4.addComment(comment5);
        repository.save(comment1);
        repository.save(comment4);
        //when
        Long mainCommentCount = repository.getMainCommentCount();
        //then
        assertThat(mainCommentCount).isEqualTo(2);
    }
}