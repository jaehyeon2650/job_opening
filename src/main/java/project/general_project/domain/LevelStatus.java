package project.general_project.domain;

public enum LevelStatus {
    BEGINNER("초급"),INTERMEDIATE("중급"),ADVANCED("고급");

    private String description;

    LevelStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
