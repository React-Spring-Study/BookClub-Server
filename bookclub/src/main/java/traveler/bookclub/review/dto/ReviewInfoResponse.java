package traveler.bookclub.review.dto;

import lombok.*;
import traveler.bookclub.review.domain.Review;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewInfoResponse {

    private Long reviewId;
    private String title;
    private String content;
    private String isbn;
    private String writerName;
    private String createdDate;

    public static ReviewInfoResponse toDto(Review review) {
        return new ReviewInfoResponse(
                review.getId(),
                review.getTitle(),
                review.getContent(),
                review.getBook().getIsbn(),
                review.getMember().getNickname(),
                review.getCreatedDate()
        );
    }
}
