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
    CLUB_FULL_MEMBER("클럽의 최대 인원을 초과하였습니다. 더 이상 가입할 수 없습니다."),
    CLUB_DUPLICATED_MEMBER("이미 가입된 멤버입니다. 같은 클럽에 중복으로 가입할 수 없습니다."),
    CLUB_MAX_TOO_SMALL("최대 가입 인원이 현재 가입 인원보다 적습니다. 현재 가입 인원과 같거나 더 큰 수를 입력해주세요.");

    private String defaultErrorMessage;
}
