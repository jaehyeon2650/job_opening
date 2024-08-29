package project.general_project.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Member {
    @Id @GeneratedValue
    private Long id;
    private String username;
    private String userId;
    private String password;
    private String phone;
    private String email;
    @Embedded
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    private Member(String username, String userId, String password,String phone,String email, Address address) {
        this.username = username;
        this.userId = userId;
        this.password = password;
        this.address = address;
        this.phone=phone;
        this.email=email;
    }
    public static Member createMember(String username, String userId, String password,String phone,String email, Address address){
        Member member=new Member(username,userId,password,phone,email,address);
        return member;
    }
    public static Member createMember(Long memberId,String username,String phone,String email, Address address){
        Member member=new Member(username,null,null,phone,email,address);
        member.setId(memberId);
        return member;
    }
    public Member() {

    }
}
