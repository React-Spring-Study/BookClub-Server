package traveler.bookclub.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import traveler.bookclub.club.Club;
import traveler.bookclub.review.domain.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByClub(Club club);
}
