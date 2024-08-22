package project.general_project.repository.comment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.general_project.domain.Comment;
import project.general_project.domain.QComment;
import project.general_project.domain.QMember;

import java.util.List;

import static project.general_project.domain.QComment.*;
import static project.general_project.domain.QMember.*;

@Repository
public class CommentRepository {
    private final EntityManager em;
    private final JPAQueryFactory query;

    public CommentRepository(EntityManager em) {
        this.em = em;
        query=new JPAQueryFactory(em);
    }

    public void save(Comment comment){
        em.persist(comment);
    }
    public Comment findById(Long id){
        return em.find(Comment.class,id);
    }

    public List<Comment> findCommentByPost(Long postId,int index,int size){
        return query.selectFrom(comment)
                .join(comment.member, member).fetchJoin()
                .where(comment.post.id.eq(postId).and(comment.parent.isNull()))
                .orderBy(comment.created.desc())
                .offset(index*size)
                .limit(size)
                .fetch();
    }

    public List<Comment> findCommentByParentId(Long parentId){
        return query.selectFrom(comment)
                .join(comment.member,member).fetchJoin()
                .where(comment.parent.id.eq(parentId))
                .orderBy(comment.created.desc()).fetch();
    }

    public Long getMainCommentCount(){
        List<Long> fetch = query.select(comment.count()).from(comment)
                .where(comment.parent.isNull())
                .fetch();
        return fetch.get(0);
    }
}
