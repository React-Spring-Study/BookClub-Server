package traveler.bookclub.club;

import jakarta.persistence.*;
import lombok.*;
import traveler.bookclub.entity.Member;

import java.util.UUID;

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

    @Column(name = "club_num")
    private Integer num;

    @Setter
    @Column(name = "club_link")
    private String link;

    @Column
    private String cid;

    @Column(name = "club_img")
    private String imgUrl;

    @JoinColumn(name = "host_id")
    @ManyToOne
    private Member host;

    @Builder
    public Club(String name, String information, Integer max, Integer num, String imgUrl, Member member) {
        this.name = name;
        this.information = information;
        this.max = max;
        this.num = num;
        this.imgUrl = imgUrl;
        this.host = member;
        this.cid = UUID.randomUUID().toString();
    }
}
