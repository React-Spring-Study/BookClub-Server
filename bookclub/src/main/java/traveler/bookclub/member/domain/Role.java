package traveler.bookclub.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public enum Role {
    MEMBER("회원");

    private String name;
}
