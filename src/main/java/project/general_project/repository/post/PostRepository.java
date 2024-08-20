package project.general_project.repository.post;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.general_project.domain.Comment;
import project.general_project.domain.Post;

import java.util.List;

@Repository
public class PostRepository {
    private final EntityManager em;
    private final JPAQueryFactory query;

    public PostRepository(EntityManager em) {
        this.em = em;
        query=new JPAQueryFactory(em);
    }

    public void save(Post post){
        em.persist(post);
    }
    public Post findById(Long postId){
        return em.find(Post.class,postId);
    }
    public Post findByIdWithMember(Long postId){
        return em.createQuery("select p from Post p join fetch p.member m where p.id=:postId",Post.class)
                .setParameter("postId",postId)
                .getSingleResult();
    }
    public void saveComment(Long postId, Comment comment){
        Post findPost = findById(postId);
        findPost.addComment(comment);
        em.persist(comment);
    }

    public Comment findCommentById(Long commentId){
        return em.find(Comment.class,commentId);
    }

    public List<Post> getPosts(int start,int count){
        return em.createQuery("select p from Post p", Post.class)
                .setFirstResult(start*10)
                .setMaxResults(count)
                .getResultList();
    }
}
