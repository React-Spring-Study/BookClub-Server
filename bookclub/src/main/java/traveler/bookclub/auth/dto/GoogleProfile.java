package traveler.bookclub.auth.dto;

import lombok.*;

import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoogleProfile {

    private String id;

    @Setter
    private String name;
    private String email;
    private String imgUrl;

    @Setter
    private Long memberId;

    public GoogleProfile(String id, String name, String email, String imgUrl) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.imgUrl = imgUrl;
    }

    public static GoogleProfile toProfile(Map<String, Object> attributes) {
        return new GoogleProfile(
                (String) attributes.get("id"),
                (String) attributes.get("name"),
                (String) attributes.get("email"),
                (String) attributes.get("picture")
        );
    }
}
