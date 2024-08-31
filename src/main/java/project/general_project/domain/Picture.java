package project.general_project.domain;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Picture {
    private String originalName;
    private String saveName;

    public Picture(String originalName, String saveName) {
        this.originalName = originalName;
        this.saveName = saveName;
    }

    public Picture() {

    }
}
