package traveler.bookclub.club.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import traveler.bookclub.club.domain.Club;
import traveler.bookclub.club.dto.ClubInfoResponse;
import traveler.bookclub.club.dto.ClubSaveRequest;
import traveler.bookclub.club.exception.ClubException;
import traveler.bookclub.club.repository.ClubRepository;
import traveler.bookclub.member.domain.Member;
import traveler.bookclub.member.service.MemberService;
import traveler.bookclub.review.service.S3Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;
    private final MemberService memberService;
    private final S3Service s3Service;

    @Value("${backend.address}")
    private String address;

    @Transactional
    public String createClub(ClubSaveRequest request, MultipartFile multipartFile) throws IOException {
        Member member = memberService.findCurrentMember();
        Club club = ClubSaveRequest.toEntity(request, member);
        String url = null;
        if (! multipartFile.isEmpty())
            url = s3Service.uploadClubImage(club.getCid(), multipartFile);
        club.setImgUrl(url);
        clubRepository.save(club);
        return club.getCid();
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
