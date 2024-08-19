package project.general_project.web.memberForm;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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

    private String firstPhone;
    private String SecondPhone;
    private String ThirdPhone;
    @Email
    private String email;
    @Length(min = 6)
    private String zipcode;
    @NotBlank
    private String city;
    @NotBlank
    private String detailAddress;
}
