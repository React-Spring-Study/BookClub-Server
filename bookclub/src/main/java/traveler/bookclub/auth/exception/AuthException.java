package traveler.bookclub.auth.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class AuthException extends RuntimeException{

    private AuthErrorCode errorCode;
    private String errorMessage;

    public AuthException(AuthErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDefaultMessage();
    }

    public AuthException(AuthErrorCode errorCode, String errorMessage) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
