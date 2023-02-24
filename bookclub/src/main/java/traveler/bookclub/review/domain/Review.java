package traveler.bookclub.review.domain;

import jakarta.persistence.*;
import lombok.*;
import traveler.bookclub.club.domain.Club;
import traveler.bookclub.entity.BaseTimeEntity;
import traveler.bookclub.member.domain.Member;

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

    @Embedded
    private BookInfo book;

    @Setter
    @Column(name = "img_url")
    private String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Review(String title, String content, BookInfo book, Club club, Member member) {
        this.title = title;
        this.content = content;
        this.book = book;
        this.club = club;
        this.member = member;
    }

    public Review updateReview(String title, String content, BookInfo book) {
        this.title = title;
        this.content = content;
        this.book = book;
        return this;
    }
}
