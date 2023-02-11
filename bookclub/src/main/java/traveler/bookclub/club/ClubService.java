package traveler.bookclub.club;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;

    @Transactional
    public Long createClub(ClubSaveRequest request) {
        Club entity = ClubSaveRequest.toEntity(request);
        return clubRepository.save(entity).getId();
    }
}
