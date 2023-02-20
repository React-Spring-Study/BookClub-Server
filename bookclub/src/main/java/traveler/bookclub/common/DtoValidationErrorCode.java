package traveler.bookclub.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum DtoValidationErrorCode {

    BAD_INPUT("올바른 입력값이 아닙니다.");

    private String defaultErrorMessage;
}
