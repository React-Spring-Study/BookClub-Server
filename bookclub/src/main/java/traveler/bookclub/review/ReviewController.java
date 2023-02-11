package traveler.bookclub.review;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import traveler.bookclub.common.StringResponse;

@RequestMapping("/review")
@RequiredArgsConstructor
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public StringResponse createReview(ReviewSaveRequest request) {
        Long reviewId = reviewService.saveReview(request);
        return new StringResponse("리뷰를 성공적으로 저장했습니다. 리뷰 ID: "+reviewId.toString());
    }
}
