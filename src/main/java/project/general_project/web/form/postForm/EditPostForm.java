package project.general_project.web.form.postForm;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import project.general_project.domain.LevelStatus;
import project.general_project.domain.RecruitmentStatus;

@Data
public class EditPostForm {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private RecruitmentStatus status;
    private Long postId;
    private LevelStatus levelStatus;

    public EditPostForm(String title, String content, RecruitmentStatus status, Long postId, LevelStatus levelStatus) {
        this.title = title;
        this.content = content;
        this.status = status;
        this.postId = postId;
        this.levelStatus = levelStatus;
    }
}
