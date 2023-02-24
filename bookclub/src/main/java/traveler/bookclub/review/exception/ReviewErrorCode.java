package traveler.bookclub.review.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum ReviewErrorCode {

    REVIEW_NOT_FOUND("해당 리뷰를 찾을 수 없습니다."),
    REVIEW_WRITER_AUTH("해당 리뷰 작성자가 아니므로 접근 권한이 없습니다.");

    private String defaultErrorMessage;
}
