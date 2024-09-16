package project.general_project.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

@Slf4j
public class CustomAuthFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage=null;

        if(exception instanceof UsernameNotFoundException){
            errorMessage="해당 아이디가 존재하지 않습니다.";
        }
        else if(exception instanceof BadCredentialsException){
            errorMessage="비밀번호가 맞지 않습니다.";
        }


        log.info("Authentication 실패: " + errorMessage); // 로그 추가
        request.setAttribute("errorMessage",errorMessage);
        request.getRequestDispatcher("/loginFail").forward(request,response);

    }
}
