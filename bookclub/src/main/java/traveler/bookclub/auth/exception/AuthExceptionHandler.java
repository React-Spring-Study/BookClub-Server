package traveler.bookclub.auth.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import traveler.bookclub.common.response.ErrorEntity;

@Slf4j
@RestControllerAdvice
public class AuthExceptionHandler {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthException.class)
    public ErrorEntity authException(AuthException ex) {
        log.error("AuthException[{}]: {}", ex.getErrorCode().toString(), ex.getErrorMessage());
        return new ErrorEntity(ex.getErrorCode().toString(), ex.getErrorMessage());
    }
}
