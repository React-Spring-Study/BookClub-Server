package traveler.bookclub.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import traveler.bookclub.club.Club;
import traveler.bookclub.club.ClubException;
import traveler.bookclub.club.ClubRepository;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ClubRepository clubRepository;

    @Transactional
    public Long saveReview(ReviewSaveRequest request) {
        Club club = clubRepository.findByCid(request.getCid())
                .orElseThrow(() -> new ClubException());
        Review entity = ReviewSaveRequest.toEntity(request);
        entity.setClub(club);
        return entity.getId();
    }
}
