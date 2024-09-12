package project.general_project.web.form.memberForm;

import lombok.Getter;
import lombok.Setter;
import project.general_project.domain.Member;
import project.general_project.domain.Post;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MemberForm {
    private Long id;
    private String username;
    private String firstPhone;
    private String SecondPhone;
    private String ThirdPhone;
    private String email;
    private String zipcode;
    private String city;
    private String detailAddress;
    private String teamName;
    private double score;
    private Long teamId;
    private List<PostDto> posts = new ArrayList<>();
    private String savedPath;

    public MemberForm(Member member, List<Post> posts, String savedPath, double score) {
        this.score = score;
        this.id = member.getId();
        this.username = member.getUsername();
        String phone = member.getPhone();
        this.firstPhone = phone.substring(0, 3);
        SecondPhone = phone.substring(3, 7);
        ThirdPhone = phone.substring(7);
        this.email = member.getEmail();
        this.zipcode = member.getAddress().getZipcode();
        this.city = member.getAddress().getCity();
        this.detailAddress = member.getAddress().getDetailAddress();
        if (member.getTeam() != null) {
            this.teamName = member.getTeam().getName();
            this.teamId = member.getTeam().getId();
        }
        for (Post post : posts) {
            this.posts.add(new PostDto(post.getTitle(), post.getId()));
        }
        this.savedPath = savedPath;
    }

    @Getter
    @Setter
    static class PostDto {
        private String name;
        private Long postId;

        public PostDto(String name, Long postId) {
            this.name = name;
            this.postId = postId;
        }
    }
}
