package traveler.bookclub.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import traveler.bookclub.common.StringResponse;
import traveler.bookclub.review.dto.ReviewInfoResponse;
import traveler.bookclub.review.dto.ReviewSaveRequest;
import traveler.bookclub.review.service.ReviewService;

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

    @GetMapping("/{reviewId}")
    public ReviewInfoResponse readReview(@PathVariable Long reviewId) {
        return reviewService.readReviewInfo(reviewId);
    }
}