package project.general_project.web.form.comment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentForm {
    private Long id;
    private String content;
    private String username;
    private LocalDateTime created;
}
