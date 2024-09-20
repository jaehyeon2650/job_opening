package project.general_project.auth.provider;

public interface OAuth2UserInfo {
    public String getUserId();
    public String getEmail();
    public String getName();
    public String getProvider();
}
