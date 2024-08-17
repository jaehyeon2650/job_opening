package project.general_project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.general_project.domain.Member;
import project.general_project.repository.member.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository repository;
    private final PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    @Transactional
    public Long save(Member member) {
        Optional<Member> findMember = repository.findByUserID(member.getUserId());
        if (findMember.isPresent()) {
            return -1L;
        }
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        repository.save(member);
        return member.getId();
    }

    public Member findById(Long id) {
        return repository.findById(id);
    }
}
