package project.general_project.web.form.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentAddForm {
    @NotBlank
    private String content;
}
