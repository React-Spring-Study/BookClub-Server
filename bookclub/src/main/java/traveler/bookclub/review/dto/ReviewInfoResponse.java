package traveler.bookclub.review.dto;

import lombok.*;
import traveler.bookclub.review.domain.Review;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewInfoResponse {

    private String title;
    private String content;
    private String isbn;

    private String createdDate;

    public static ReviewInfoResponse of(Review review) {
        return new ReviewInfoResponse(review.getTitle(), review.getContent(), review.getIsbn(), review.getCreatedDate().toString());
    }
}