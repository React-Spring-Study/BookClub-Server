package traveler.bookclub.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import traveler.bookclub.club.domain.Club;
import traveler.bookclub.club.exception.ClubException;
import traveler.bookclub.club.repository.ClubRepository;
import traveler.bookclub.review.repository.ReviewRepository;
import traveler.bookclub.review.domain.Review;
import traveler.bookclub.review.dto.ReviewInfoResponse;
import traveler.bookclub.review.dto.ReviewSaveRequest;
import traveler.bookclub.review.exception.ReviewException;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ClubRepository clubRepository;

    @Transactional
    public Long saveReview(ReviewSaveRequest request) {
        Club club = clubRepository.findByCid(request.getCid())
                .orElseThrow(() -> new ClubException());
        Review entity = ReviewSaveRequest.toEntity(request);
        entity.setClub(club);
        return entity.getId();
    }

    @Transactional
    public ReviewInfoResponse readReviewInfo(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException());
        return ReviewInfoResponse.of(review);
    }
}
