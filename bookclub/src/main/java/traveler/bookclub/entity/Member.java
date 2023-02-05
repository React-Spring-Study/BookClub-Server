package traveler.bookclub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private Long id;

    @NotBlank
    private String nickname;

    @Column(name = "sub", unique = true)
    @NotBlank
    private String userId;
    private String password;
    private String email;
    @NotNull
    private String emailVerifiedYn;
    @Enumerated(EnumType.STRING)
    private Role role;
}
