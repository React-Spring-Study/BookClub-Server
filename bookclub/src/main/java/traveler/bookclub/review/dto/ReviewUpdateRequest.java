package traveler.bookclub.review.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import traveler.bookclub.review.domain.BookInfo;
import traveler.bookclub.review.domain.Review;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewUpdateRequest {

    @NotNull
    private Long reviewId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    private String bookTitle;

    @NotBlank
    private String bookIsbn;

    public void updateReview(Review target) {
        target.updateReview(this.title, this.content, new BookInfo(this.bookTitle, this.bookIsbn));
    }
}
