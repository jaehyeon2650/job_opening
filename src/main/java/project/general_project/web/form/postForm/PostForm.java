package project.general_project.web.form.postForm;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.general_project.domain.RecruitmentStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PostForm {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private RecruitmentStatus status;
    private Long postId;
    private String writer;
    private LocalDateTime create;

    public PostForm(String title, String content,RecruitmentStatus status,Long postId,String writer,LocalDateTime create) {
        this.title = title;
        this.content = content;
        this.status=status;
        this.postId=postId;
        this.writer=writer;
        this.create=create;
    }

    public PostForm(String title, String content,RecruitmentStatus status,Long postId) {
        this.title = title;
        this.content = content;
        this.status=status;
        this.postId=postId;

    }
}
