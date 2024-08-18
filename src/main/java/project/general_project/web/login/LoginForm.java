package project.general_project.web.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginForm {
    @NotBlank
    private String id;
    @NotBlank
    private String password;
}
