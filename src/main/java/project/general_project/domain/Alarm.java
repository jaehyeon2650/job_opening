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
    private boolean read;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    private LocalDateTime time;
}
