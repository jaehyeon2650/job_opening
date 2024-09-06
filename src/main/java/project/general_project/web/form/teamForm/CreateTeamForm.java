package project.general_project.web.form.teamForm;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class CreateTeamForm {
    @NotBlank
    private String name;
    private List<String> members;
}
