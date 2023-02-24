package traveler.bookclub.auth.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import traveler.bookclub.auth.domain.AuthToken;
import traveler.bookclub.auth.domain.MemberRefreshToken;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthInfo {

    private AuthToken accessToken;
    private MemberRefreshToken memberRefreshToken;
}
