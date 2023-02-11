package traveler.bookclub.review;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
                .build();
    }
}
