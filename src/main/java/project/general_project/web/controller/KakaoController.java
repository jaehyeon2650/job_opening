package project.general_project.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import project.general_project.domain.Member;
import project.general_project.service.KakaoLoginService;
import project.general_project.web.dto.KakaoInfo;
import project.general_project.web.dto.OAuthKakaoToken;

@Slf4j
@Controller
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoLoginService service;
    private final AuthenticationManager authenticationManager;

    @Value("${client_id}")
    private String client_id;

    @Value("${redirect_uri}")
    private String redirect_uri;

    @Value("${kakao_password}")
    private String password;

    @GetMapping("/auth/kakao/callback")
    public String kakaoLogin(@RequestParam String code, HttpServletRequest request) throws JsonProcessingException {
        RestTemplate rt=new RestTemplate();
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String,String> params=new LinkedMultiValueMap<>();
        params.add("grant_type","authorization_code");
        params.add("client_id",client_id);
        params.add("redirect_uri",redirect_uri);
        params.add("code",code);

        HttpEntity<MultiValueMap<String,String>> httpEntity=new HttpEntity<>(params,httpHeaders);
        ResponseEntity<String> response = rt.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST, httpEntity, String.class);

        ObjectMapper objectMapper=new ObjectMapper();
        OAuthKakaoToken token = objectMapper.readValue(response.getBody(), OAuthKakaoToken.class);

        httpHeaders=new HttpHeaders();
        httpHeaders.add("Authorization","Bearer "+token.getAccess_token());
        httpHeaders.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        httpEntity=new HttpEntity<>(httpHeaders);
        response=rt.exchange("https://kapi.kakao.com/v2/user/me",HttpMethod.POST,httpEntity,String.class);
        KakaoInfo memberInfo = objectMapper.readValue(response.getBody(), KakaoInfo.class);

        Member member = service.loginWithKakao(memberInfo.getId(), memberInfo.getProperties().getNickname());

        HttpSession session = request.getSession();
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(member.getUserId(),password));
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,context);

        return "redirect:/";
    }
}
