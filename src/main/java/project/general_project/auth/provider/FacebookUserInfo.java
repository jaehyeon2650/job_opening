package project.general_project.auth.provider;

import java.util.Map;

public class FacebookUserInfo implements OAuth2UserInfo{

    private final Map<String,Object> attributes;

    public FacebookUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getUserId() {
        return getProvider()+"_"+attributes.get("id");
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
        return "facebook";
    }
}
