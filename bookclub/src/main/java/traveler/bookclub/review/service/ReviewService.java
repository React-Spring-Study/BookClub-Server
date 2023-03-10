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
import traveler.bookclub.common.util.S3Service;
import traveler.bookclub.member.domain.Member;
import traveler.bookclub.member.service.MemberService;
import traveler.bookclub.review.dto.*;
import traveler.bookclub.review.exception.ReviewErrorCode;
import traveler.bookclub.review.repository.ReviewRepository;
import traveler.bookclub.review.domain.Review;
import traveler.bookclub.review.exception.ReviewException;

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
    public Long saveReview(ReviewSaveRequest request, MultipartFile multipartFile) {
        Member member = memberService.findCurrentMember();
        Club club = clubRepository.findById(request.getClubId())
                .orElseThrow(() -> new ClubException(ClubErrorCode.CLUB_NOT_FOUND));
        clubService.verifyClubMember(member, request.getClubId());
        Review review = reviewRepository.save(ReviewSaveRequest.toEntity(request, member, club));
        String url = null;
        if (! multipartFile.isEmpty())
            url = s3Service.uploadReviewImage(review.getId(), multipartFile);
        review.setImgUrl(url);
        return review.getId();
    }

    @Transactional(readOnly = true)
    public List<ReviewListDto> readReviewListByClub(Long clubId, Pageable pageable) {
        Member member = memberService.findCurrentMember();
        Club club = clubRepository.findById(clubId).orElseThrow(
                () -> new ClubException(ClubErrorCode.CLUB_NOT_FOUND)
        );
        clubService.verifyClubMember(member, clubId);
        return ReviewListDto.toDtoList(reviewRepository.findAllByClub(club, pageable));
    }

    @Transactional(readOnly = true)
    public List<MyReviewListDto> readMyReviewList(Pageable pageable) {
        Member me = memberService.findCurrentMember();
        return MyReviewListDto.toMyReviewDtoList(reviewRepository.findAllByMember(me, pageable));
    }

    @Transactional(readOnly = true)
    public ReviewInfoResponse readReviewInfo(Long reviewId) {
        Member member = memberService.findCurrentMember();
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND));
        clubService.verifyClubMember(member, review.getClub().getId());

        return ReviewInfoResponse.toDto(review);
    }

    @Transactional
    public void updateReview(ReviewUpdateRequest request) {
        Member member = memberService.findCurrentMember();
        Review target = reviewRepository.findById(request.getReviewId())
                .orElseThrow(() -> new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND));
        // ?????? ?????? ?????? ??? ??????
        verifyReviewWriter(member, target);
        request.updateReview(target);
    }

    /**
     * ?????? ???????????? ??????
     *  img ?????? ?????? ?????? img set
     *  img ?????? ?????? ?????? img ??????
     * */
    @Transactional
    public void updateReviewImage(Long reviewId, MultipartFile img) {
        Member member = memberService.findCurrentMember();
        Review target = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND));
        // ?????? ?????? ??????
        verifyReviewWriter(member, target);

        // ??????????????? ??????
        String url = target.getImgUrl();
        if(! img.isEmpty()) {
            if (url != null)
                s3Service.deleteImage(url);
            target.setImgUrl(s3Service.uploadReviewImage(target.getId(), img));
        } else if (url!=null) {
            // ????????? ????????? ?????? ???????????? ????????? ?????? -> ????????? ??????
            s3Service.deleteImage(url);
            target.setImgUrl(null);
        }
    }

    private void verifyReviewWriter(Member member, Review review) {
        if (! member.equals(review.getMember()))
            throw new ReviewException(ReviewErrorCode.REVIEW_WRITER_AUTH);
        return;
    }
}
