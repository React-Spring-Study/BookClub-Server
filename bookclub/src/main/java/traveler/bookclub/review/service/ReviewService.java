package traveler.bookclub.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import traveler.bookclub.club.domain.Club;
import traveler.bookclub.club.exception.ClubException;
import traveler.bookclub.club.repository.ClubRepository;
import traveler.bookclub.review.domain.BookInfo;
import traveler.bookclub.review.repository.BookInfoRepository;
import traveler.bookclub.review.repository.ReviewRepository;
import traveler.bookclub.review.domain.Review;
import traveler.bookclub.review.dto.ReviewInfoResponse;
import traveler.bookclub.review.dto.ReviewSaveRequest;
import traveler.bookclub.review.exception.ReviewException;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ClubRepository clubRepository;
    private final BookInfoRepository bookInfoRepository;

    @Transactional
    public Long saveReview(ReviewSaveRequest request) {
        Club club = clubRepository.findByCid(request.getCid())
                .orElseThrow(() -> new ClubException());
        BookInfo bookInfo;
        Optional<BookInfo> optional = bookInfoRepository.findByIsbn(request.getBook().getIsbn());
        if (optional.isEmpty()) {
            bookInfo = bookInfoRepository.save(new BookInfo(request.getBook().getTitle(), request.getBook().getIsbn()));
        } else {
            bookInfo = optional.get();
        }
        Review entity = ReviewSaveRequest.toEntity(request);
        entity.setBook(bookInfo);
        entity.setClub(club);
        return reviewRepository.save(entity).getId();
    }

    @Transactional
    public ReviewInfoResponse readReviewInfo(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException());
        return ReviewInfoResponse.toDto(review);
    }
}
