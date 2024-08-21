package project.general_project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.general_project.domain.Comment;
import project.general_project.domain.Post;
import project.general_project.repository.post.PostRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository repository;

    @Transactional
    public Long save(Post post){
        repository.save(post);
        return post.getId();
    }

    public Optional<Post> findById(Long postId){
        return Optional.of(repository.findById(postId));
    }
    public Optional<Post> findByIdWithMember(Long postId){
        return Optional.of(repository.findByIdWithMember(postId));
    }
    @Transactional
    public void saveComment(Long postId, Comment comment){
        repository.saveComment(postId,comment);
    }

    @Transactional
    public Long updatePost(Post post){
        Post findPost = repository.findById(post.getId());
        findPost.setTitle(post.getTitle());
        findPost.setUpdated(LocalDateTime.now());
        findPost.setStatus(post.getStatus());
        findPost.setContent(post.getContent());
        return findPost.getId();
    }
    public List<Post> findPosts(int start,int count){
        return repository.getPosts(start,count);
    }

    public List<Post> findPostsByTitle(String content,int start,int count){
        return repository.getPostsByTitle(content,start,count);
    }
    public Long getPostCount(){
        return repository.getPostCount();
    }
}
