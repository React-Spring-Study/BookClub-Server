package traveler.bookclub.club.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubListDto {

    private Long clubId;
    private String name;
    private String hostName;
    private String imgUrl;
}
