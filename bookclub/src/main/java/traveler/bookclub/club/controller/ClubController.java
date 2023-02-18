package traveler.bookclub.club.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import traveler.bookclub.club.service.ClubService;
import traveler.bookclub.club.dto.ClubInfoResponse;
import traveler.bookclub.club.dto.ClubSaveRequest;
import traveler.bookclub.clubMember.ClubMemberTest;
import traveler.bookclub.common.response.StringResponse;

import java.io.IOException;
import java.util.List;

@RequestMapping("/club")
@RequiredArgsConstructor
@RestController
public class ClubController {

    private final ClubService clubService;

    @PostMapping
    public StringResponse createClub(@RequestPart ClubSaveRequest request, MultipartFile img) throws IOException {
        String cid = clubService.createClub(request, img);

        return new StringResponse("새로운 모임이 결성되었습니다. 클럽 ID: " + cid);
    }

    @GetMapping("/{cid}")
    public ClubInfoResponse showClubInfo(@PathVariable String cid) {
        return clubService.showClubInfo(cid);
    }

    @GetMapping("/new/{cid}")
    public StringResponse joinRequest(@PathVariable String cid) {
        clubService.joinClub(cid);
        return new StringResponse("모임에 가입되셨습니다. 클럽 ID" + cid);
    }

    //TODO: 없애기
    @GetMapping("/test")
    public List<ClubMemberTest> showClubs() {
        return clubService.test();
    }
}
