package traveler.bookclub.club.exception;

import lombok.Getter;

@Getter
public class ClubException extends RuntimeException{

    private ClubErrorCode errorCode;
    private String errorMessage;

    public ClubException(ClubErrorCode errorCode) {
        super(errorCode.getDefaultErrorMessage());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDefaultErrorMessage();
    }

    public ClubException(ClubErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.errorMessage = message;
    }
}
