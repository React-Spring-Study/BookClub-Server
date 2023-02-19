package traveler.bookclub.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import traveler.bookclub.common.response.StringResponse;
import traveler.bookclub.review.dto.ReviewInfoResponse;
import traveler.bookclub.review.dto.ReviewListDto;
import traveler.bookclub.review.dto.ReviewSaveRequest;
import traveler.bookclub.review.service.ReviewService;

import java.io.IOException;
import java.util.List;

@RequestMapping("/review")
@RequiredArgsConstructor
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public StringResponse createReview(@RequestPart ReviewSaveRequest request, MultipartFile img) throws IOException {
        Long reviewId = reviewService.saveReview(request, img);
        return new StringResponse("리뷰를 성공적으로 저장했습니다. 리뷰 ID: " + reviewId.toString());
    }

    @GetMapping("/club/{clubId}")
    public List<ReviewListDto> readReviewsByCid(@PathVariable Long clubId, Pageable pageable) {
        return reviewService.readReviewListByClub(clubId, pageable);
    }

    @GetMapping("/{clubId}/{reviewId}")
    public ReviewInfoResponse readReview(@PathVariable Long clubId, @PathVariable Long reviewId) {
        return reviewService.readReviewInfo(clubId, reviewId);
    }
}
