package traveler.bookclub.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Role {
    MEMBER("회원"),
    GUEST("손님");

    private String name;

    public static Role of(String name) {
        return Arrays.stream(Role.values())
                .filter(r -> r.getName().equals(name))
                .findAny()
                .orElse(GUEST);
    }
}
