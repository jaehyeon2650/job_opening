package project.general_project.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.general_project.auth.provider.FacebookUserInfo;
import project.general_project.auth.provider.GoogleUserInfo;
import project.general_project.auth.provider.NaverUserInfo;
import project.general_project.auth.provider.OAuth2UserInfo;
import project.general_project.domain.Member;
import project.general_project.repository.member.MemberRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PrincipleOauth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Value("${oauth_password}")
    private String password;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println(userRequest);
        System.out.println(userRequest.getAccessToken().getTokenValue());
        System.out.println(userRequest.getClientRegistration().getRegistrationId());
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User.getAttributes());

        OAuth2UserInfo oAuth2UserInfo=null;
        String provider=userRequest.getClientRegistration().getRegistrationId();
        if(provider.equals("google")){
            oAuth2UserInfo=new GoogleUserInfo(oAuth2User.getAttributes());
        }else if(provider.equals("facebook")){
            oAuth2UserInfo=new FacebookUserInfo(oAuth2User.getAttributes());
        }else if(provider.equals("naver")){
            oAuth2UserInfo=new NaverUserInfo((Map<String, Object>) oAuth2User.getAttributes().get("response"));
        }

        String email= oAuth2UserInfo.getEmail();
        String userId= oAuth2UserInfo.getUserId();
        String name= oAuth2UserInfo.getName();

        Optional<Member> findMember = repository.findByUserID(userId);
        Member member=null;
        if(findMember.isEmpty()){
            member=Member.createMember(name,userId,passwordEncoder.encode(password),null,email,null);
            member.setOauth(userRequest.getClientRegistration().getRegistrationId());
            repository.save(member);
        }else{
            member=findMember.get();
        }

        return new PrincipalDetails(member,oAuth2User.getAttributes());
    }
}
