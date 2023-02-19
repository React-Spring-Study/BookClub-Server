package traveler.bookclub.club.domain;

import jakarta.persistence.*;
import lombok.*;
import traveler.bookclub.clubMember.ClubMember;
import traveler.bookclub.member.domain.Member;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "club")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Club {

    @Id @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "club_id")
    private Long id;

    @Column(name = "club_name")
    private String name;

    @Column(name = "club_info")
    private String information;

    @Column(name = "club_max")
    private Integer max;

    @Setter
    @Column(name = "club_num")
    private Integer num;

    @Column(name = "club_link")
    private String link;

    @Setter
    @Column(name = "club_img")
    private String imgUrl;

    @JoinColumn(name = "host_id")
    @ManyToOne
    private Member host;

    @OneToMany(mappedBy = "club")
    private List<ClubMember> members = new ArrayList<>();

    @Builder
    public Club(String name, String information, Integer max, Integer num, String link, String imgUrl, Member host) {
        this.name = name;
        this.information = information;
        this.max = max;
        this.num = num;
        this.link = link;
        this.imgUrl = imgUrl;
        this.host = host;
    }
}
