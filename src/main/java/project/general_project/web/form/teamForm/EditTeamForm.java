package project.general_project.web.form.teamForm;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import project.general_project.domain.Team;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
public class EditTeamForm {
    @NotBlank
    private String teamName;
    private String leaderUserId;
    private List<String> members = new ArrayList<>();

    public EditTeamForm(Team team) {
        this.teamName = team.getName();
        this.leaderUserId = team.getLeader().getUserId();
        team.getMembers().forEach(o -> {
            members.add(o.getUserId());
        });
    }

    public EditTeamForm(String teamName, String leaderUserId, List<String> members) {
        this.teamName = teamName;
        this.leaderUserId = leaderUserId;
        this.members = members;
    }

    public EditTeamForm() {
    }
}
