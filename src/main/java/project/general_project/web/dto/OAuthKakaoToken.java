package project.general_project.web.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter @Setter
public class OAuthKakaoToken {
    public String access_token;
    public String token_type;
    public String refresh_token;
    public String id_token;
    public int expires_in;
    public String scope;
    public int refresh_token_expires_in;

}
