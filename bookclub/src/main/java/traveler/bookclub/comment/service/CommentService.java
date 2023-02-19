package traveler.bookclub.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import traveler.bookclub.club.service.ClubService;
import traveler.bookclub.comment.domain.Comment;
import traveler.bookclub.comment.dto.CommentResponse;
import traveler.bookclub.comment.dto.CommentSaveRequest;
import traveler.bookclub.comment.repository.CommentRepository;
import traveler.bookclub.member.domain.Member;
import traveler.bookclub.member.service.MemberService;
import traveler.bookclub.review.domain.Review;
import traveler.bookclub.review.exception.ReviewException;
import traveler.bookclub.review.repository.ReviewRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final MemberService memberService;
    private final ClubService clubService;
    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public Long saveComment(CommentSaveRequest request) {
        Review review = reviewRepository.findById(request.getReviewId())
                .orElseThrow(() -> new ReviewException());
        Member member = memberService.findCurrentMember();
        clubService.verifyClubMember(member, review.getClub().getId());
        Comment save = commentRepository.save(
                Comment.builder()
                        .content(request.getContent())
                        .review(review)
                        .member(member)
                        .build()
        );
        return save.getId();
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> readCommentsByReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException());
        Member member = memberService.findCurrentMember();
        clubService.verifyClubMember(member, review.getClub().getId());

        List<CommentResponse> response = new ArrayList<>();
        for (Comment comment : commentRepository.findAllByReview(review)) {
            CommentResponse commentResponse = new CommentResponse(
                    comment.getContent(),
                    comment.getMember().getNickname(),
                    comment.getCreatedDate()
            );
            response.add(commentResponse);
        }
        return response;
    }
}
