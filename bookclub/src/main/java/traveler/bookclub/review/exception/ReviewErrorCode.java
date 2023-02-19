package traveler.bookclub.review.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum ReviewErrorCode {

    REVIEW_NOT_FOUND("해당 리뷰를 찾을 수 없습니다.");

    private String defaultErrorMessage;
}
