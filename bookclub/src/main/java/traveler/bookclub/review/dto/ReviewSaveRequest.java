package traveler.bookclub.review.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import traveler.bookclub.club.domain.Club;
import traveler.bookclub.member.domain.Member;
import traveler.bookclub.review.domain.BookInfo;
import traveler.bookclub.review.domain.Review;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewSaveRequest {

    private String title;
    private String content;
    private String bookTitle;
    private String bookIsbn;
    private Long clubId;

    public static Review toEntity(ReviewSaveRequest request, Member member, Club club) {
        return Review.builder()
                .title(request.title)
                .content(request.content)
                .book(new BookInfo(request.getBookTitle(), request.getBookIsbn()))
                .member(member)
                .club(club)
                .build();
    }
}
