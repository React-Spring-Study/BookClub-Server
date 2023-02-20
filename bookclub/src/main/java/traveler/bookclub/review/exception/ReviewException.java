package traveler.bookclub.review.exception;

import lombok.Getter;

@Getter
public class ReviewException extends RuntimeException{

    private ReviewErrorCode errorCode;
    private String errorMessage;

    public ReviewException(ReviewErrorCode errorCode) {
        super(errorCode.getDefaultErrorMessage());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDefaultErrorMessage();
    }

    public ReviewException(ReviewErrorCode errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
