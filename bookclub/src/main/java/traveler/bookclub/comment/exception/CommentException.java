package traveler.bookclub.comment.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentException extends RuntimeException{

    private CommentErrorCode errorCode;
    private String errorMessage;

    public CommentException(CommentErrorCode errorCode) {
        super(errorCode.getDefaultErrorMessage());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDefaultErrorMessage();
    }

    public CommentException(CommentErrorCode errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
