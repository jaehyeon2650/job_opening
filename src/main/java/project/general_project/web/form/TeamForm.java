package project.general_project.web.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class TeamForm {
    @NotBlank
    private String name;
    private List<String> members;
}
