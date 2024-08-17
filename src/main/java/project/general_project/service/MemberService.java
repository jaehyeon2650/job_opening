package project.general_project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.general_project.domain.Member;
import project.general_project.repository.member.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository repository;

    @Transactional
    public Long save(Member member){
        repository.save(member);
        return member.getId();
    }
    public Member findById(Long id){
        return repository.findById(id);
    }
}
