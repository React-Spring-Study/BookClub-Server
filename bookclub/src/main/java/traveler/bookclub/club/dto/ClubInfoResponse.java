package traveler.bookclub.club.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import traveler.bookclub.club.domain.Club;
import traveler.bookclub.clubMember.ClubMember;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubInfoResponse {

    private Long clubId;
    private String name;
    private String hostName;
    private String imgUrl;
    private Integer max;
    private Integer num;
    private List<String> members;

    public ClubInfoResponse(String name, String imgUrl) {
        this.name = name;
        this.imgUrl = imgUrl;
    }

    public static ClubInfoResponse of(Club club) {
        return new ClubInfoResponse(
                club.getId(),
                club.getName(),
                club.getHost().getNickname(),
                club.getImgUrl(),
                club.getMax(),
                club.getNum(),
                toNameList(club.getMembers())
        );
    }

    private static List<String> toNameList(List<ClubMember> list) {
        List<String> names = new ArrayList<>();
        for (ClubMember clubMember : list) {
            names.add(clubMember.getMember().getNickname());
        }
        return names;
    }
}
