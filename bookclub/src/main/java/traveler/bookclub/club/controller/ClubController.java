package traveler.bookclub.club.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import traveler.bookclub.club.service.ClubService;
import traveler.bookclub.club.dto.ClubInfoResponse;
import traveler.bookclub.club.dto.ClubSaveRequest;
import traveler.bookclub.common.StringResponse;

@RequestMapping("/club")
@RequiredArgsConstructor
@RestController
public class ClubController {

    private final ClubService clubService;

    @PostMapping
    public StringResponse createClub(@RequestBody ClubSaveRequest request) {
        Long clubId = clubService.createClub(request);

        return new StringResponse("새로운 모임이 결성되었습니다. 클럽 ID=" + clubId.toString());
    }

    @GetMapping("/{cid}")
    public ClubInfoResponse showClubInfo(@PathVariable String cid) {
        return clubService.showClubInfo(cid);
    }
}
