package traveler.bookclub.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import traveler.bookclub.entity.BaseTimeEntity;
import traveler.bookclub.clubMember.ClubMember;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private Long id;

    private String nickname;

    @Column(name = "sub", unique = true)
    private String username;
    private String password;
    private String email;
    private String profileUrl;

    @OneToMany(mappedBy = "member")
    private List<ClubMember> clubs = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(String nickname, String username, String password, String email, String profileUrl) {
        this.nickname = nickname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = Role.MEMBER;
        this.profileUrl = profileUrl;
    }

}
