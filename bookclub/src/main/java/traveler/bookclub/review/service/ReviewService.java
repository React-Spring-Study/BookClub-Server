package traveler.bookclub.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import traveler.bookclub.club.domain.Club;
import traveler.bookclub.club.exception.ClubException;
import traveler.bookclub.club.repository.ClubRepository;
import traveler.bookclub.member.service.MemberService;
import traveler.bookclub.review.dto.ReviewListDto;
import traveler.bookclub.review.repository.ReviewRepository;
import traveler.bookclub.review.domain.Review;
import traveler.bookclub.review.dto.ReviewInfoResponse;
import traveler.bookclub.review.dto.ReviewSaveRequest;
import traveler.bookclub.review.exception.ReviewException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ClubRepository clubRepository;
    private final MemberService memberService;

    @Transactional
    public Long saveReview(ReviewSaveRequest request) {
        Club club = clubRepository.findByCid(request.getCid())
                .orElseThrow(() -> new ClubException());
        Review entity = ReviewSaveRequest.toEntity(request, memberService.findCurrentMember(), club);
        return reviewRepository.save(entity).getId();
    }

    @Transactional
    public List<ReviewListDto> readReviewListByClub(String clubId, Pageable pageable) {
        Club club = clubRepository.findByCid(clubId).orElseThrow(
                () -> new ClubException()
        );
        // TODO: 현재 로그인 멤버 클럽 가입여부 확인
        return ReviewListDto.toDtoList(reviewRepository.findAllByClub(club, pageable));
    }

    @Transactional
    public ReviewInfoResponse readReviewInfo(String clubId, Long reviewId) {
        // TODO: 현재 로그인 멤버 클럽 가입여부 확인
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException());
        return ReviewInfoResponse.toDto(review);
    }
}
