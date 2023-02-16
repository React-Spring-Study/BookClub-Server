package traveler.bookclub.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import traveler.bookclub.review.domain.BookInfo;

import java.util.Optional;

public interface BookInfoRepository extends JpaRepository<BookInfo, Long> {

    Optional<BookInfo> findByIsbn(String isbn);
    Optional<BookInfo> findByTitle(String title);
}
