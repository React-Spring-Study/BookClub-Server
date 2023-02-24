package traveler.bookclub.auth.dto;

import lombok.*;

import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoogleProfile {

    private String id;

    @Setter
    private String name;
    private String email;
    private String imgUrl;

    public static GoogleProfile toProfile(Map<String, Object> attributes) {
        return new GoogleProfile(
                (String) attributes.get("id"),
                (String) attributes.get("name"),
                (String) attributes.get("email"),
                (String) attributes.get("picture")
        );
    }
}
