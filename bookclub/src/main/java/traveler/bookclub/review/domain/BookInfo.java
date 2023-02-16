package traveler.bookclub.review.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import traveler.bookclub.review.domain.Review;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookInfo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;
    private String title;
    private String isbn;

    public BookInfo(String title, String isbn) {
        this.title = title;
        this.isbn = isbn;
    }
}
