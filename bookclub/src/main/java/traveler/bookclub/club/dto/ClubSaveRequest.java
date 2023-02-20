package traveler.bookclub.club.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank
    private String name;
    @NotNull
    private String information;
    @NotNull
    @Min(value = 1)
    private Integer max;

    private String link;

    public static Club toEntity(ClubSaveRequest request, Member member, String imgUrl) {
        return Club.builder()
                .name(request.name)
                .information(request.information)
                .max(request.max)
                .link(request.link)
                .num(0)
                .host(member)
                .imgUrl(imgUrl)
                .build();
    }
}
