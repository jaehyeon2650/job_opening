package project.general_project.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Assessment {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_id")
    private Member toMember;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_id")
    private Member fromMember;
    private int score;
    private String content;

    public Assessment(Member toMember, Member fromMember, int score, String content) {
        this.toMember = toMember;
        this.fromMember = fromMember;
        this.score = score;
        this.content = content;
    }

    public Assessment() {

    }
}
