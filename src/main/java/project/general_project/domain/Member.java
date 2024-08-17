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
    @Embedded
    private Address address;

    private Member(String username, String userId, String password, Address address) {
        this.username = username;
        this.userId = userId;
        this.password = password;
        this.address = address;
    }
    public static Member createMember(String username, String userId, String password, Address address){
        Member member=new Member(username,userId,password,address);
        return member;
    }
    public Member() {

    }
}
