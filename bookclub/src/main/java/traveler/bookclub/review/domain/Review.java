package traveler.bookclub.review.domain;

import jakarta.persistence.*;
import lombok.*;
import traveler.bookclub.club.domain.Club;
import traveler.bookclub.entity.BaseTimeEntity;

@Getter
@Entity
@Table(name = "review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {

    @Id @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "review_id")
    private Long id;

    @Column(name = "review_title")
    private String title;

    @Column(name = "review_content")
    private String content;

    @Setter
    @ManyToOne
    @JoinColumn(name = "book_id")
    private BookInfo book;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @Builder
    public Review(String title, String content, BookInfo book, Club club) {
        this.title = title;
        this.content = content;
        this.book = book;
        this.club = club;
    }
}
