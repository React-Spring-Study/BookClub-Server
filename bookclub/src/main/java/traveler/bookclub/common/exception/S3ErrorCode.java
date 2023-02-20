package traveler.bookclub.common.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum S3ErrorCode {

    S3_UPLOAD_FAILED("이미지 업로드에 실패하였습니다."),
    S3_DELETE_FAILED("이미지 삭제에 실패하였습니다.");

    private String defaultErrorMessage;
}
