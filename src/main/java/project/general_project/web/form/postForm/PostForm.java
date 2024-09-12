package project.general_project.web.form.postForm;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.general_project.domain.LevelStatus;
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
    private LevelStatus levelStatus;

    public PostForm(String title, String content, RecruitmentStatus status, Long postId, String writer, LocalDateTime create, LevelStatus levelStatus) {
        this.title = title;
        this.content = content;
        this.status = status;
        this.postId = postId;
        this.writer = writer;
        this.create = create;
        this.levelStatus = levelStatus;
    }

    public PostForm(String title, String content, RecruitmentStatus status, Long postId, LevelStatus levelStatus) {
        this.title = title;
        this.content = content;
        this.status = status;
        this.postId = postId;
        this.levelStatus = levelStatus;
    }
}
