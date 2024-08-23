package project.general_project.repository.post;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import project.general_project.domain.Comment;
import project.general_project.domain.Post;

import java.util.List;

import static com.querydsl.core.types.dsl.Expressions.*;
import static project.general_project.domain.QPost.*;

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

    public Comment findCommentById(Long commentId){
        return em.find(Comment.class,commentId);
    }

    public List<Post> getPosts(int start,int count){
        return em.createQuery("select p from Post p", Post.class)
                .setFirstResult(start*10)
                .setMaxResults(count)
                .getResultList();
    }

    public List<Post> getPostsByTitle(String content,int start,int count){
        return query
                .selectFrom(post)
                .where(titleLike(content))
                .orderBy(post.status.desc())
                .offset(start*10)
                .limit(count)
                .fetch();
    }

    public Long getPostCount(){
        List<Long> fetch = query.select(post.count()).from(post).fetch();
        Long count = fetch.get(0);
        return count;
    }

    private BooleanExpression titleLike(String content){
        return content != null? stringTemplate("lower({0})",post.title).like("%"+content.toLowerCase()+"%"):null;
    }
}
