package traveler.bookclub.club.service;

import lombok.RequiredArgsConstructor;
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
import traveler.bookclub.clubMember.ClubMemberResponse;
import traveler.bookclub.member.domain.Member;
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

    @Transactional
    public Long createClub(ClubSaveRequest request, MultipartFile multipartFile) throws IOException {
        Member member = memberService.findCurrentMember();
        String url = null;
        if (! multipartFile.isEmpty())
            url = s3Service.uploadClubImage(multipartFile);
        Club club = ClubSaveRequest.toEntity(request, member, url);
        addClubMember(member, club);
        return clubRepository.save(club).getId();
    }

    @Transactional(readOnly = true)
    public ClubInfoResponse showClubInfo(Long cid) {
        Club club = clubRepository.findById(cid)
                .orElseThrow(() -> new ClubException(ClubErrorCode.CLUB_NOT_FOUND));
        return ClubInfoResponse.of(club);
    }

    @Transactional(readOnly = true)
    public void verifyClubMember(Member member, Long cid) {
        clubMemberRepository.findByMemberAndClub_Id(member, cid)
                .orElseThrow(() -> new ClubException(ClubErrorCode.CLUB_NO_AUTH));
    }

    @Transactional
    public void joinClub(Long cid) {
        Member member = memberService.findCurrentMember();
        Club club = clubRepository.findById(cid)
                .orElseThrow(() -> new ClubException(ClubErrorCode.CLUB_NOT_FOUND));
        addClubMember(member, club);
    }

    private void addClubMember(Member member, Club club) {
        // 최대 인원 초과 검사
        if (club.getNum() == club.getMax())
            throw new ClubException(ClubErrorCode.CLUB_FULL_MEMBER);
        // 중복 가입 검사
        if (clubMemberRepository.findByMemberAndClub_Id(member, club.getId()).isPresent())
            throw new ClubException(ClubErrorCode.CLUB_DUPLICATED_MEMBER);
        // 검사 모두 통과하면 추가
        ClubMember clubMember = new ClubMember(club, member);
        member.getClubs().add(clubMember);
        club.getMembers().add(clubMember);
        club.setNum(club.getNum() + 1);
        clubMemberRepository.save(clubMember);
    }

    @Transactional
    public List<ClubMemberResponse> readMyClubs() {
        Member currentMember = memberService.findCurrentMember();
        List<ClubMemberResponse> objects = new ArrayList<>();
        for (ClubMember entity : currentMember.getClubs()) {
            Club club = clubRepository.findById(entity.getClub().getId())
                    .orElseThrow(() -> new ClubException(ClubErrorCode.CLUB_NOT_FOUND));
            objects.add(ClubMemberResponse.toDto(club));
        }
        return objects;
    }
}
