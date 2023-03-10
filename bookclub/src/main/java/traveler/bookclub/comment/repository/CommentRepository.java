package traveler.bookclub.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;
import traveler.bookclub.comment.domain.Comment;
import traveler.bookclub.member.domain.Member;
import traveler.bookclub.review.domain.Review;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Repository> {

    Optional<Comment> findById(Long commentId);
    List<Comment> findAllByReview(Review review);
    List<Comment> findAllByMember(Member member);
}
