package traveler.bookclub.member.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum MemberErrorCode {
    MEMBER_NOT_FOUND("회원을 찾을 수 없습니다."),
    MEMBER_DUPLICATED("이미 존재하는 회원입니다.");

    private String defaultErrorMessage;
}
