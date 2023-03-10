package traveler.bookclub.club.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import traveler.bookclub.club.domain.Club;
import traveler.bookclub.member.domain.Member;

import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Long> {

    Optional<Club> findAllByHost(Member host);
}
