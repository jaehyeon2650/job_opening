package project.general_project.web.join;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class JoinForm {
    @NotBlank
    private String username;
    @NotBlank
    private String userId;
    @NotBlank
    private String password;
    private String passwordCheck;
    @NotBlank
    @Length(min = 6)
    private String zipcode;
    @NotBlank
    private String city;
    @NotBlank
    private String detailAddress;
}
