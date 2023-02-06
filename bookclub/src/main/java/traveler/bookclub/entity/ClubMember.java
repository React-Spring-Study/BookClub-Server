package traveler.bookclub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import traveler.bookclub.club.Club;

@Getter
@Entity
@Table(name = "club_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubMember {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public ClubMember(Club club, Member member) {
        this.club = club;
        this.member = member;
    }
}
