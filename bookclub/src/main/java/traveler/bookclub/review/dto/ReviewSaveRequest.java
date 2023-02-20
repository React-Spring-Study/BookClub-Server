package traveler.bookclub.review.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    private String bookTitle;

    @NotBlank
    private String bookIsbn;

    @NotNull
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
