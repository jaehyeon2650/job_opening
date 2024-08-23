package project.general_project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.general_project.domain.Comment;
import project.general_project.repository.comment.CommentRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository repository;

    @Transactional
    public void saveInPost(Comment comment){
        repository.save(comment);
    }

    @Transactional
    public void saveInComment(Comment comment,Long parentId){
        Comment parent = repository.findById(parentId);
        parent.addComment(comment);
        repository.save(comment);
    }

    public List<Comment> findCommentByPost(Long postId,int start,int size){
        return repository.findCommentByPost(postId,start,size);
    }

    public List<Comment> findCommentByParentId(Long parentId){
        return repository.findCommentByParentId(parentId);
    }

    public Long getMainCommentCount(Long postId){
        return repository.getMainCommentCount(postId);
    }
}
