package traveler.bookclub.club.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import traveler.bookclub.club.domain.Club;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubInfoResponse {

    private String name;
    private String hostName;
    private String imgUrl;

    public ClubInfoResponse(String name, String imgUrl) {
        this.name = name;
        this.imgUrl = imgUrl;
    }

    public static ClubInfoResponse of(Club club) {
        return new ClubInfoResponse(
                club.getName(),
                club.getHost().getNickname(),
                club.getImgUrl()
        );
    }
}
