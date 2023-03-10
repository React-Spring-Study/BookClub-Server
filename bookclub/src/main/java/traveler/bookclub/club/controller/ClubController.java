package traveler.bookclub.club.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import traveler.bookclub.club.dto.ClubUpdateRequest;
import traveler.bookclub.club.service.ClubService;
import traveler.bookclub.club.dto.ClubInfoResponse;
import traveler.bookclub.club.dto.ClubSaveRequest;
import traveler.bookclub.clubMember.ClubMemberResponse;
import traveler.bookclub.common.response.StringResponse;

import java.util.List;

@RequestMapping("/clubs")
@RequiredArgsConstructor
@RestController
public class ClubController {

    private final ClubService clubService;

    @PostMapping
    public StringResponse createClub(@Valid @RequestPart ClubSaveRequest request, MultipartFile img) {
        Long cid = clubService.createClub(request, img);

        return new StringResponse("새로운 모임이 결성되었습니다. 클럽 ID: " + cid.toString());
    }

    @GetMapping("/{cid}")
    public ClubInfoResponse showClubInfo(@PathVariable Long cid) {
        return clubService.showClubInfo(cid);
    }

    @GetMapping("/new/{cid}")
    public StringResponse joinRequest(@PathVariable Long cid) {
        clubService.joinClub(cid);
        return new StringResponse("모임에 가입되셨습니다. 클럽 ID: " + cid.toString());
    }

    @GetMapping("/me")
    public List<ClubMemberResponse> showClubs() {
        return clubService.readMyClubs();
    }

    @PutMapping
    public StringResponse updateClub(@Valid @RequestBody ClubUpdateRequest request) {
        clubService.updateClub(request);
        return new StringResponse("클럽 정보를 성공적으로 수정하였습니다.");
    }

    @PutMapping("/{clubId}")
    public StringResponse updateClubImage(@PathVariable Long clubId, MultipartFile img) {
        clubService.updateClubImage(clubId, img);
        return new StringResponse("클럽 대표 사진을 수정하였습니다.");
    }
}
