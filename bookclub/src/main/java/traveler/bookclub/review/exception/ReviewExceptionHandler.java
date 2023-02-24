package traveler.bookclub.review.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import traveler.bookclub.common.response.ErrorEntity;

@Slf4j
@RestControllerAdvice
public class ReviewExceptionHandler {

    @ExceptionHandler(ReviewException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorEntity onReviewException(ReviewException ex) {
        log.error("ReviewException[{}]: {}", ex.getErrorCode().toString(), ex.getErrorMessage());
        return new ErrorEntity(ex.getErrorCode().toString(), ex.getErrorMessage());
    }
}
