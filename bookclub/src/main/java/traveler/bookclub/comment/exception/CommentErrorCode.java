package traveler.bookclub.comment.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum CommentErrorCode {

    COMMENT_NOT_FOUND("해당 id의 댓글을 찾을 수 없습니다."),
    COMMENT_NO_AUTH("해당 댓글에 대한 접근 권한이 없습니다.");

    private String defaultErrorMessage;
}
