package project.general_project.auth.provider;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo{

    private final Map<String,Object> attributes;

    public NaverUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getUserId() {
        return getProvider()+"_"+getEmail();
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getProvider() {
        return "naver";
    }
}
