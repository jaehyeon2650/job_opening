package project.general_project.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import project.general_project.domain.Member;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter @Setter
public class PrincipalDetails implements UserDetails, OAuth2User {

    private final Member member;
    private final Map<String,Object> attributes;

    public PrincipalDetails(Member member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }

    public PrincipalDetails(Member member) {
        this.member = member;
        this.attributes=null;
    }


    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return null;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUserId();
    }

}
