package traveler.bookclub.review.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import traveler.bookclub.club.domain.Club;
import traveler.bookclub.member.domain.Member;
import traveler.bookclub.review.domain.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByClub(Club club, Pageable pageable);

    List<Review> findAllByMember(Member member, Pageable pageable);
}
