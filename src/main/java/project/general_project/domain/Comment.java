package project.general_project.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Comment {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent",orphanRemoval = true,cascade = CascadeType.ALL)
    private List<Comment> comments= new ArrayList<>();
    @Lob
    private String content;

    private LocalDateTime created;


    public void addComment(Comment comment){
        comments.add(comment);
        comment.setParent(this);
    }

    public static Comment createComment(Member member,String content,Post post){
        Comment comment=new Comment();
        comment.setMember(member);
        comment.setPost(post);
        comment.setContent(content);
        comment.setCreated(LocalDateTime.now());
        return comment;
    }

}
