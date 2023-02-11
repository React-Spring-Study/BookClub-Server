package traveler.bookclub.club;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubInfoResponse {

    private String name;
    private String hostName;
    private String imgUrl;

    public static ClubInfoResponse of(Club club) {
        return new ClubInfoResponse(
                club.getName(),
                club.getHost().getNickname(),
                club.getImgUrl()
        );
    }
}
