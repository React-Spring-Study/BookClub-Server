package traveler.bookclub.club;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;

    @Transactional
    public Long createClub(ClubSaveRequest request) {
        Club entity = ClubSaveRequest.toEntity(request);
        return clubRepository.save(entity).getId();
    }

    @Transactional
    public ClubInfoResponse showClubInfo(String cid) {
        Club club = clubRepository.findByCid(cid)
                .orElseThrow(() -> new ClubException());
        return ClubInfoResponse.of(club);
    }

    @Transactional
    public void readGroupMembers(String cid) {
        //
    }
}
