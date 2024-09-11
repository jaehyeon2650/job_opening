package project.general_project.web.form.assessmentForm;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Data
@Getter @Setter
public class AssessmentForm {
    private String toMemberId;
    private String fromMemberId;
    @NotEmpty
    private String content;
    @Range(min=0,max = 10)
    private int score;

    public AssessmentForm() {
    }

    public AssessmentForm(String toMemberId, String content, int score,String fromMemberId) {
        this.toMemberId = toMemberId;
        this.content = content;
        this.score = score;
        this.fromMemberId=fromMemberId;
    }

    public AssessmentForm(String toMemberId,String fromMemberId) {
        this.toMemberId = toMemberId;
        this.fromMemberId=fromMemberId;
    }
}
