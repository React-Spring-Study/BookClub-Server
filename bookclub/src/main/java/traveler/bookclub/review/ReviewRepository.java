package traveler.bookclub.review;

import org.springframework.data.jpa.repository.JpaRepository;
import traveler.bookclub.club.Club;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByClub(Club club);
}
