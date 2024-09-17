package project.general_project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.general_project.domain.Member;
import project.general_project.repository.member.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KakaoLoginService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${kakao_password}")
    private String password;

    @Transactional
    public Member loginWithKakao(Long id,String name){
        String userId="kakao_"+id;
        Optional<Member> findMember = memberRepository.findByUserID(userId);
        Member member=null;
        if(findMember.isEmpty()){
            member = Member.createMember(name, userId, passwordEncoder.encode(password), null, null, null);
            member.setOauth("kakao");
            memberRepository.save(member);
        }else{
            member=findMember.get();
        }
        return member;
    }
}
