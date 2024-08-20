package project.general_project.web.postForm;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import project.general_project.domain.RecruitmentStatus;

@Data
public class EditPostForm {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private RecruitmentStatus status;
    private Long postId;

    public EditPostForm(String title, String content, RecruitmentStatus status, Long postId) {
        this.title = title;
        this.content = content;
        this.status = status;
        this.postId = postId;
    }
}
