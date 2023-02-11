package traveler.bookclub.club;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import traveler.bookclub.common.StringResponse;

@RequestMapping("/club")
@RequiredArgsConstructor
@RestController
public class ClubController {

    private final ClubService clubService;

    @PostMapping("/create")
    public StringResponse createClub(@RequestBody ClubSaveRequest request) {
        Long clubId = clubService.createClub(request);

        return new StringResponse("새로운 모임이 결성되었습니다. 클럽 ID=" + clubId.toString());
    }
}
