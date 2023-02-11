package traveler.bookclub.review.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import traveler.bookclub.review.domain.Review;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewSaveRequest {

    private String title;
    private String content;
    private String isbn;
    private String cid;

    public static Review toEntity(ReviewSaveRequest request) {
        return Review.builder()
                .title(request.title)
                .content(request.content)
                .isbn(request.isbn)
                .build();
    }
}
