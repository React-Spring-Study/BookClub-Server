package traveler.bookclub.club.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import traveler.bookclub.club.domain.Club;
import traveler.bookclub.member.domain.Member;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubSaveRequest {

    private String name;
    private String information;
    private Integer max;
    private String imgUrl;

    public static Club toEntity(ClubSaveRequest request, Member member) {
        return Club.builder()
                .name(request.name)
                .information(request.information)
                .max(request.max)
                .num(0)
                .host(member)
                .imgUrl(request.imgUrl != null ? request.imgUrl : "NA")
                .build();
    }
}
