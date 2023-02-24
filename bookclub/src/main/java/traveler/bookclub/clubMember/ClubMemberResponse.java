package traveler.bookclub.clubMember;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import traveler.bookclub.club.domain.Club;
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubMemberResponse {

    private Long clubId;
    private String name;
    private String hostName;
    private String imgUrl;

    public static ClubMemberResponse toDto(Club club) {
        return new ClubMemberResponse(
                club.getId(),
                club.getName(),
                club.getHost().getNickname(),
                club.getImgUrl()
        );
    }
}
