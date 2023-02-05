package traveler.bookclub.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BasicResponse<T> {
    private boolean success;
    private T response;
    private ErrorEntity error;
}
