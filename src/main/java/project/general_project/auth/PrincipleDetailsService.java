package project.general_project.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.general_project.domain.Member;
import project.general_project.repository.member.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrincipleDetailsService implements UserDetailsService {

    private final MemberRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> member = repository.findByUserID(username);
            if(member.isPresent()) return new PrincipalDetails(member.get());
            throw new UsernameNotFoundException("해당 유저가 없습니다 username: " + username);
    }
}
