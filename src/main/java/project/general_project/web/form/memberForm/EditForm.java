package project.general_project.web.form.memberForm;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;
import project.general_project.domain.Member;

@Data
@NoArgsConstructor
public class EditForm {
    private Long id;
    @NotBlank
    private String username;
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
    private MultipartFile multipartFile;
    private String savedUrl;

    public EditForm(Member member,String savedUrl) {
        this.id=member.getId();
        this.username = member.getUsername();
        String phone = member.getPhone();
        this.firstPhone = phone.substring(0,3);;
        SecondPhone = phone.substring(3,7);
        ThirdPhone = phone.substring(7);
        this.email = member.getEmail();
        this.zipcode = member.getAddress().getZipcode();
        this.city = member.getAddress().getCity();
        this.detailAddress = member.getAddress().getDetailAddress();
        this.savedUrl=savedUrl;
    }

}
