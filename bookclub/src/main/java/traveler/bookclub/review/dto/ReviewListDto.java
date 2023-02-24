package traveler.bookclub.review.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import traveler.bookclub.review.domain.Review;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewListDto {

    private Long reviewId;
    private String title;
    private String writerName;
    private String isbn;
    private String createdDate;

    public static List<ReviewListDto> toDtoList(List<Review> entityList) {
        List<ReviewListDto> dtos = new ArrayList<>();
        for (Review review : entityList) {
            dtos.add(new ReviewListDto(
                    review.getId(),
                    review.getTitle(),
                    review.getMember().getNickname(),
                    review.getBook().getIsbn(),
                    review.getCreatedDate())
            );
        }
        return dtos;
    }
}
