package project.general_project.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
    public Member() {

    }
}
