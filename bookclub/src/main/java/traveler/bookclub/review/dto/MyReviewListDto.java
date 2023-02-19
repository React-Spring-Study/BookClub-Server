package traveler.bookclub.review.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import traveler.bookclub.club.dto.ClubListDto;
import traveler.bookclub.review.domain.Review;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyReviewListDto {

    private Long reviewId;
    private String title;
    private ClubListDto club;
    private String isbn;
    private String createdDate;

    public static List<MyReviewListDto> toMyReviewDtoList(List<Review> entityList) {
        List<MyReviewListDto> dtos = new ArrayList<>();
        for (Review review : entityList) {
            dtos.add(new MyReviewListDto(
                    review.getId(),
                    review.getTitle(),
                    ClubListDto.of(review.getClub()),
                    review.getBook().getIsbn(),
                    review.getCreatedDate())
            );
        }
        return dtos;
    }
}
