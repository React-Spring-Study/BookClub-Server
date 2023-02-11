package traveler.bookclub.member.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import traveler.bookclub.entity.BaseTimeEntity;

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
    private String userId;
    private String password;
    private String email;
    private String profileUrl;
    @NotNull
    private String emailVerifiedYn;
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(String nickname, String userId, String password, String email, String profileUrl) {
        this.nickname = nickname;
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.emailVerifiedYn = "Y";
        this.role = Role.MEMBER;
        this.profileUrl = profileUrl != null ? profileUrl : "noImg";
    }

    // TODO: 유효성 검증은 엔티티가 아닌 DTO에서!!!
}
