package traveler.bookclub.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "review_id")
    private Long id;

    @Column(name = "review_title")
    private String title;

    @Column(name = "review_content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    @Builder
    public Review(String title, String content, Club club) {
        this.title = title;
        this.content = content;
        this.club = club;
    }
}
