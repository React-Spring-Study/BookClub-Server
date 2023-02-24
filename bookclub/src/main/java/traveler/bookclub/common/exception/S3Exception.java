package traveler.bookclub.common.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class S3Exception extends RuntimeException{

    private S3ErrorCode errorCode;
    private String errorMessage;

    public S3Exception(S3ErrorCode errorCode) {
        super(errorCode.getDefaultErrorMessage());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDefaultErrorMessage();
    }

    public S3Exception(S3ErrorCode errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
