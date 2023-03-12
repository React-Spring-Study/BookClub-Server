package traveler.bookclub.club.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubUpdateRequest {

    private Long clubId;

    @NotBlank
    private String name;

    private String information;

    @Min(value = 1)
    private Integer max;

    private String link;
}
