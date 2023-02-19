package traveler.bookclub.clubMember;

import org.springframework.data.jpa.repository.JpaRepository;
import traveler.bookclub.club.domain.Club;
import traveler.bookclub.member.domain.Member;

import java.util.Optional;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {

    Optional<ClubMember> findByMemberAndClub_Id(Member member, Long clubId);
}
