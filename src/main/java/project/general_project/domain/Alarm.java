package project.general_project.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Alarm {
    @Id @GeneratedValue
    private Long id;
    private String content;
    private boolean readCheck;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    private Long fromMemberId;
    private LocalDateTime time;

    public Alarm() {}

    public Alarm(Member member,Long fromMemberId, String content){
        this.member=member;
        this.content=content;
        this.fromMemberId=fromMemberId;
        this.time=LocalDateTime.now();
        this.readCheck=false;
    }
}
