package project.general_project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.general_project.domain.Member;
import project.general_project.domain.Picture;
import project.general_project.repository.member.MemberRepository;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository repository;
    private final PictureStore pictureStore;
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
    public Member findByUserId(String userId){
        return repository.findByUserID(userId).get();
    }

    @Transactional
    public Long updateMember(Member member){
        Member findMember = repository.findById(member.getId());
        findMember.setUsername(member.getUsername());
        findMember.setPhone(member.getPhone());
        findMember.setAddress(member.getAddress());
        findMember.setEmail(member.getEmail());
        return findMember.getId();
    }

    public Member findByIdWithTeam(Long id){
        return repository.findByIdWithTeam(id);
    }

    @Transactional
    public void updatePicture(Long id, MultipartFile multipartFile) throws IOException {
        Member findMember = repository.findById(id);
        Picture picture = pictureStore.savePicture(multipartFile);
        findMember.setPicture(picture);
    }
}
