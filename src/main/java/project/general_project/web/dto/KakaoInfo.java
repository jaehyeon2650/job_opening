package project.general_project.web.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class KakaoInfo {
    @Data
    public class Properties{
        public String nickname;
    }
    @Data
    public class KakaoAccount{
        @Data
        public class Profile{
            public String nickname;
            public Boolean is_default_nickname;
        }

        public Boolean profile_nickname_needs_agreement;
        public Profile profile;
    }

    public Long id;
    public String connected_at;
    public Properties properties;
    public KakaoAccount kakao_account;

}
