package traveler.bookclub.club;

import org.springframework.data.jpa.repository.JpaRepository;
import traveler.bookclub.entity.Member;

public interface ClubRepository extends JpaRepository<Club, Long> {

    <Optional>Club findAllByHost(Member host);
}
