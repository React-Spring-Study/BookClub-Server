package traveler.bookclub.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "comment_content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Comment(String content, Review review, Member member) {
        this.content = content;
        this.review = review;
        this.member = member;
    }
}
