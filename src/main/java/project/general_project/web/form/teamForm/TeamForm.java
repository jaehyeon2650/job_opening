package project.general_project.web.form.teamForm;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import project.general_project.domain.Team;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Getter
public class TeamForm {
    private Long teamId;
    private String teamName;
    private String leaderUserId;
    private Long leaderId;
    private List<MemberDto> members;

    public TeamForm(Team team) {
        this.teamId = team.getId();
        this.teamName = team.getName();
        this.leaderUserId = team.getLeader().getUserId();
        this.leaderId = team.getLeader().getId();
        members = team.getMembers().stream().map(o -> {
            return new MemberDto(o.getId(), o.getUserId());
        }).collect(Collectors.toList());
    }


    @Data
    @Getter
    @Setter
    static class MemberDto {
        private Long Id;
        private String userId;

        public MemberDto(Long id, String userId) {
            Id = id;
            this.userId = userId;
        }
    }
}
