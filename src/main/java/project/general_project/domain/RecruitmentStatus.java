package project.general_project.domain;

public enum RecruitmentStatus {
    READY("모집 중"), COMP("모집 완료");
    private final String description;

    RecruitmentStatus(String description) {
        this.description = description;
    }
    public String getDescription(){
        return description;
    }
}
