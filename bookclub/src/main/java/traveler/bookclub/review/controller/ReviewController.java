package traveler.bookclub.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import traveler.bookclub.common.response.StringResponse;
import traveler.bookclub.review.dto.*;
import traveler.bookclub.review.service.ReviewService;

@RequestMapping("/reviews")
@RequiredArgsConstructor
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public StringResponse createReview(@Valid @RequestPart ReviewSaveRequest request, MultipartFile img) {
        Long reviewId = reviewService.saveReview(request, img);
        return new StringResponse("리뷰를 성공적으로 저장했습니다. 리뷰 ID: " + reviewId.toString());
    }

    @GetMapping("/club/{clubId}")
    public ClubReviewPageResponse readReviewsByCid(@PathVariable Long clubId, Pageable pageable) {
        return reviewService.readReviewListByClub(clubId, pageable);
    }

    @GetMapping("/{reviewId}")
    public ReviewInfoResponse readReview(@PathVariable Long reviewId) {
        return reviewService.readReviewInfo(reviewId);
    }

    @GetMapping("/me")
    public MyReviewPageResponse readMyReviews(Pageable pageable) {
        return reviewService.readMyReviewList(pageable);
    }

    @PutMapping
    public StringResponse updateReview(@RequestBody ReviewUpdateRequest request) {
        reviewService.updateReview(request);
        return new StringResponse("리뷰를 수정하였습니다.");
    }

    @PutMapping("/{reviewId}")
    public StringResponse updateReviewImage(@PathVariable Long reviewId, MultipartFile img) {
        reviewService.updateReviewImage(reviewId, img);
        return new StringResponse("리뷰 이미지를 수정하였습니다.");
    }
}
