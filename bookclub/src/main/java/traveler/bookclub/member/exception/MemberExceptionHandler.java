package traveler.bookclub.member.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import traveler.bookclub.common.response.ErrorEntity;

@Slf4j
@RestControllerAdvice
public class MemberExceptionHandler {

    @ExceptionHandler(MemberException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorEntity onMemberException(MemberException ex) {
        log.error("MemberException[{}]: {}", ex.getErrorCode().toString(), ex.getErrorMessage());
        return new ErrorEntity(ex.getErrorCode().toString(), ex.getErrorMessage());
    }
}
