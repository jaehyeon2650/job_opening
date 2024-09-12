package project.general_project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.general_project.domain.Member;
import project.general_project.repository.member.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Member login(String loginId, String password) {
        Member member = memberRepository.findByUserID(loginId).orElse(null);
        if (member == null) {
            return null;
        }
        if (!passwordEncoder.matches(password, member.getPassword())) {
            return null;
        }

        return member;
    }
}
