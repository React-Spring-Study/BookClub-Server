package traveler.bookclub.review.dto;

import lombok.*;
import traveler.bookclub.member.domain.Member;
import traveler.bookclub.member.dto.WriterInfo;
import traveler.bookclub.review.domain.Review;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewInfoResponse {

    private Long reviewId;
    private String title;
    private String content;
    private String isbn;
    private WriterInfo writer;
    private String imgUrl;
    private String createdDate;

    public static ReviewInfoResponse toDto(Review review) {
        Member member = review.getMember();
        return new ReviewInfoResponse(
                review.getId(),
                review.getTitle(),
                review.getContent(),
                review.getBook().getIsbn(),
                new WriterInfo(member.getNickname(), member.getProfileUrl()),
                review.getImgUrl(),
                review.getCreatedDate()
        );
    }
}
