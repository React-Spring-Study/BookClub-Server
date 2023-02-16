package traveler.bookclub.review.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import traveler.bookclub.review.domain.BookInfo;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookInfoDto {

    private String title;
    private String isbn;

    public static BookInfoDto toDto(BookInfo entity) {
        return new BookInfoDto(entity.getTitle(), entity.getIsbn());
    }
}
