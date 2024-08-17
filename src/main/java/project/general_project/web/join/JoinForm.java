package project.general_project.web.join;

import lombok.Data;

@Data
public class JoinForm {
    private String username;
    private String userId;
    private String password;
    private String zipcode;
    private String city;
    private String detailAddress;
}
