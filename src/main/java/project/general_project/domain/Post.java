package project.general_project.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Post {
    @Id @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    private String title;
    @Lob
    private String content;
    private LocalDateTime created;
    private LocalDateTime updated;
    @Enumerated(EnumType.STRING)
    private RecruitmentStatus status;


    public static Post createPost(Member member,String title,String content){
        Post post=new Post();
        post.setMember(member);
        post.setTitle(title);
        post.setContent(content);
        post.setCreated(LocalDateTime.now());
        post.setUpdated(LocalDateTime.now());
        post.setStatus(RecruitmentStatus.READY);
        return post;
    }
}
