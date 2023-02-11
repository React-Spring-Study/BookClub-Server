package traveler.bookclub.club;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubSaveRequest {

    private String name;
    private String information;
    private Integer max;
    private String link;
    private String imgUrl;

    public static Club toEntity(ClubSaveRequest request) {
        return Club.builder()
                .name(request.name)
                .information(request.information)
                .max(request.max)
                .num(1)
                .link(request.link)
                .imgUrl(request.imgUrl!=null ? request.imgUrl : "NA")
                .build();
    }
}
