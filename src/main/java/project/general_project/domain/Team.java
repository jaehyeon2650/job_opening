package project.general_project.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Team {
    @Id @GeneratedValue
    private Long id;

    private String name;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="leader_id")
    private Member leader;

    @OneToMany(mappedBy = "team")
    private List<Member> members=new ArrayList<>();

    public void addMember(Member member){
        members.add(member);
        member.setTeam(this);
    }
}
