package traveler.bookclub.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import traveler.bookclub.comment.domain.Comment;
import traveler.bookclub.comment.dto.CommentSaveRequest;
import traveler.bookclub.comment.repository.CommentRepository;
import traveler.bookclub.review.domain.Review;
import traveler.bookclub.review.exception.ReviewException;
import traveler.bookclub.review.repository.ReviewRepository;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public Long saveComment(CommentSaveRequest request) {
        Review review = reviewRepository.findById(request.getReviewId())
                .orElseThrow(() -> new ReviewException());
        // TODO: 사용자 정보 가져오기 작성자 설정
        Comment save = commentRepository.save(
                Comment.builder()
                        .content(request.getContent())
                        .review(review)
                        .build()
        );
        return save.getId();
    }
}
