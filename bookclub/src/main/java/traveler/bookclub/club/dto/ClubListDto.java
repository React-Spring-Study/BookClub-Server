package traveler.bookclub.club.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import traveler.bookclub.club.domain.Club;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubListDto {

    private Long clubId;
    private String name;
    private String hostName;
    private String imgUrl;

    public static ClubListDto of(Club club) {
        return new ClubListDto(club.getId(), club.getName(), club.getHost().getNickname(), club.getImgUrl());
    }
}
