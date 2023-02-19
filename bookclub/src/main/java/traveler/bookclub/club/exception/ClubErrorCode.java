package traveler.bookclub.club.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum ClubErrorCode {

    CLUB_NOT_FOUND("해당 id의 클럽을 찾을 수 없습니다."),
    CLUB_NO_AUTH("해당 클럽에 접근할 수 있는 권한이 없습니다."),
    CLUB_FULL_MEMBER("클럽의 최대 인원을 초과하였습니다. 더 이상 가입할 수 없습니다.");

    private String defaultErrorMessage;
}
