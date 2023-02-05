package traveler.bookclub.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public enum Role {
    MEMBER("회원");

    private String name;
}
