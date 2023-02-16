package traveler.bookclub.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import traveler.bookclub.review.domain.BookInfo;

public interface BookInfoRepository extends JpaRepository<BookInfo, Long> {

}
