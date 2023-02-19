package traveler.bookclub.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import traveler.bookclub.club.domain.Club;
import traveler.bookclub.club.exception.ClubErrorCode;
import traveler.bookclub.club.exception.ClubException;
import traveler.bookclub.club.repository.ClubRepository;
import traveler.bookclub.club.service.ClubService;
import traveler.bookclub.member.domain.Member;
import traveler.bookclub.member.service.MemberService;
import traveler.bookclub.review.dto.ReviewListDto;
import traveler.bookclub.review.repository.ReviewRepository;
import traveler.bookclub.review.domain.Review;
import traveler.bookclub.review.dto.ReviewInfoResponse;
import traveler.bookclub.review.dto.ReviewSaveRequest;
import traveler.bookclub.review.exception.ReviewException;

import java.io.IOException;
import java.util.List;
@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ClubRepository clubRepository;
    private final ClubService clubService;
    private final MemberService memberService;
    private final S3Service s3Service;

    @Transactional
    public Long saveReview(ReviewSaveRequest request, MultipartFile multipartFile) throws IOException {
        Member member = memberService.findCurrentMember();
        Club club = clubRepository.findByCid(request.getCid())
                .orElseThrow(() -> new ClubException(ClubErrorCode.CLUB_NOT_FOUND));
        clubService.verifyClubMember(member, club);
        String url = null;
        if (! multipartFile.isEmpty())
            url = s3Service.uploadReviewImage(multipartFile);
        Review entity = ReviewSaveRequest.toEntity(request, member, club);
        entity.setImgUrl(url);
        return reviewRepository.save(entity).getId();
    }

    @Transactional
    public List<ReviewListDto> readReviewListByClub(String clubId, Pageable pageable) {
        Member member = memberService.findCurrentMember();
        Club club = clubRepository.findByCid(clubId).orElseThrow(
                () -> new ClubException(ClubErrorCode.CLUB_NOT_FOUND)
        );
        clubService.verifyClubMember(member, club);
        return ReviewListDto.toDtoList(reviewRepository.findAllByClub(club, pageable));
    }

    @Transactional
    public ReviewInfoResponse readReviewInfo(String clubId, Long reviewId) {
        Member member = memberService.findCurrentMember();
        Club club = clubRepository.findByCid(clubId).orElseThrow(
                () -> new ClubException(ClubErrorCode.CLUB_NOT_FOUND)
        );
        clubService.verifyClubMember(member, club);
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException());
        return ReviewInfoResponse.toDto(review);
    }
}
