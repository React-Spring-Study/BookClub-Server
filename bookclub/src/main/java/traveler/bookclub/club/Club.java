package traveler.bookclub.club;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name = "club_link")
    private String link;

    @Column(name = "club_img")
    private String imgUrl;

    @Builder
    public Club(String name, String information, Integer max, Integer num, String link, String imgUrl) {
        this.name = name;
        this.information = information;
        this.max = max;
        this.num = num;
        this.link = link;
        this.imgUrl = imgUrl;
    }
}
