package traveler.bookclub.club.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import traveler.bookclub.club.domain.Club;
import traveler.bookclub.club.dto.ClubInfoResponse;
import traveler.bookclub.club.dto.ClubSaveRequest;
import traveler.bookclub.club.exception.ClubErrorCode;
import traveler.bookclub.club.exception.ClubException;
import traveler.bookclub.club.repository.ClubRepository;
import traveler.bookclub.clubMember.ClubMember;
import traveler.bookclub.clubMember.ClubMemberRepository;
import traveler.bookclub.clubMember.ClubMemberTest;
import traveler.bookclub.member.domain.Member;
import traveler.bookclub.member.exception.MemberException;
import traveler.bookclub.member.service.MemberService;
import traveler.bookclub.review.service.S3Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
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
        addClubMember(member, club);
        clubRepository.save(club);
        return club.getCid();
    }

    @Transactional
    public ClubInfoResponse showClubInfo(String cid) {
        Club club = clubRepository.findByCid(cid)
                .orElseThrow(() -> new ClubException(ClubErrorCode.CLUB_NOT_FOUND));
        return ClubInfoResponse.of(club);
    }

    @Transactional
    public void verifyClubMember(Member member, Club club) {
        clubMemberRepository.findByMemberAndClub(member, club)
                .orElseThrow(() -> new ClubException(ClubErrorCode.CLUB_NO_AUTH));
    }

    @Transactional
    public void joinClub(String cid) {
        Member member = memberService.findCurrentMember();
        Club club = clubRepository.findByCid(cid)
                .orElseThrow(() -> new ClubException(ClubErrorCode.CLUB_NOT_FOUND));
        addClubMember(member, club);
    }

    private void addClubMember(Member member, Club club) {
        if (club.getNum() == club.getMax())
            throw new ClubException(ClubErrorCode.CLUB_FULL_MEMBER);
        ClubMember clubMember = new ClubMember(club, member);
        member.getClubs().add(clubMember);
        club.getMembers().add(clubMember);
        club.setNum(club.getNum() + 1);
        clubMemberRepository.save(clubMember);
    }

    //TODO: 없애기
    @Transactional
    public List<ClubMemberTest> test() {
        Member currentMember = memberService.findCurrentMember();
        List<ClubMemberTest> objects = new ArrayList<>();
        for (ClubMember entity : currentMember.getClubs()) {
            objects.add(ClubMemberTest.toDto(entity));
        }
        return objects;
    }
}
