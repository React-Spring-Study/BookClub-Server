package traveler.bookclub.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import traveler.bookclub.auth.domain.MemberRefreshToken;

public interface MemberRefreshTokenRepository extends JpaRepository<MemberRefreshToken, Long> {

    MemberRefreshToken findByUsername(String username);
    MemberRefreshToken findByUsernameAndRefreshToken(String username, String refreshToken);

}
