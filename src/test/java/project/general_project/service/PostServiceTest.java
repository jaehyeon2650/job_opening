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

}