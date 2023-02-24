package traveler.bookclub.club.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import traveler.bookclub.common.response.ErrorEntity;

@Slf4j
@RestControllerAdvice
public class ClubExceptionHandler {

    @ExceptionHandler(ClubException.class)
    public ErrorEntity onClubException(ClubException ex) {
        log.error("ClubException[{}]: {}", ex.getErrorCode().toString(), ex.getErrorMessage());
        return new ErrorEntity(ex.getErrorCode().toString(), ex.getErrorMessage());
    }
}
