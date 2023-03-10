package traveler.bookclub.club.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import traveler.bookclub.club.domain.Club;
import traveler.bookclub.club.dto.ClubInfoResponse;
import traveler.bookclub.club.dto.ClubSaveRequest;
import traveler.bookclub.club.dto.ClubUpdateRequest;
import traveler.bookclub.club.exception.ClubErrorCode;
import traveler.bookclub.club.exception.ClubException;
import traveler.bookclub.club.repository.ClubRepository;
import traveler.bookclub.clubMember.ClubMember;
import traveler.bookclub.clubMember.ClubMemberRepository;
import traveler.bookclub.clubMember.ClubMemberResponse;
import traveler.bookclub.member.domain.Member;
import traveler.bookclub.member.service.MemberService;
import traveler.bookclub.common.util.S3Service;

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
    public Long createClub(ClubSaveRequest request, MultipartFile multipartFile){
        Member member = memberService.findCurrentMember();
        Club club = clubRepository.save(ClubSaveRequest.toEntity(request, member));
        String url = null;
        if (! multipartFile.isEmpty())
            url = s3Service.uploadClubImage(club.getId(), multipartFile);
        club.setImgUrl(url);
        addClubMember(member, club);
        return club.getId();
    }

    @Transactional(readOnly = true)
    public ClubInfoResponse showClubInfo(Long cid) {
        Club club = clubRepository.findById(cid)
                .orElseThrow(() -> new ClubException(ClubErrorCode.CLUB_NOT_FOUND));
        return ClubInfoResponse.of(club);
    }

    @Transactional
    public void joinClub(Long cid) {
        Member member = memberService.findCurrentMember();
        Club club = clubRepository.findById(cid)
                .orElseThrow(() -> new ClubException(ClubErrorCode.CLUB_NOT_FOUND));
        addClubMember(member, club);
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

    @Transactional
    public void updateClub(ClubUpdateRequest request) {
        if (! verifyClubHost(memberService.findCurrentMember(), request.getClubId())) // ????????? ?????? ??????
            throw new ClubException(ClubErrorCode.CLUB_NO_AUTH);
        Club club = clubRepository.findById(request.getClubId()).orElseThrow(
                () -> new ClubException(ClubErrorCode.CLUB_NOT_FOUND)
        );

        // ?????? ????????? ?????? ???????????? ?????? ????????? ??????
        if (request.getMax() < club.getNum())
            throw new ClubException(ClubErrorCode.CLUB_MAX_TOO_SMALL);

        club.updateClub(request.getName(), request.getInformation(), request.getMax(), request.getLink());

    }

    @Transactional
    public void updateClubImage(Long clubId, MultipartFile img) {
        if (! verifyClubHost(memberService.findCurrentMember(), clubId)) // ????????? ?????? ??????
            throw new ClubException(ClubErrorCode.CLUB_NO_AUTH);
        Club club = clubRepository.findById(clubId).orElseThrow(
                () -> new ClubException(ClubErrorCode.CLUB_NOT_FOUND)
        );

        String url = club.getImgUrl();
        // ?????? ????????? ??????
        if (! img.isEmpty()) {
            if (url != null)
                s3Service.deleteImage(url);
            // ?????? ????????? ????????? ?????????. ?????? ????????? ????????? overwrite
            club.setImgUrl(s3Service.uploadClubImage(club.getId(), img));
        } else if (url != null) {
            // ????????? ????????? ?????? ???????????? ????????? ?????? -> ????????? ??????
            s3Service.deleteImage(url);
            club.setImgUrl(null);
        }
    }

    public void verifyClubMember(Member member, Long cid) {
        clubMemberRepository.findByMemberAndClub_Id(member, cid)
                .orElseThrow(() -> new ClubException(ClubErrorCode.CLUB_NO_AUTH));
    }

    public boolean verifyClubHost(Member member, Long cid) {
        Club club = clubRepository.findById(cid).orElseThrow(
                () -> new ClubException(ClubErrorCode.CLUB_NOT_FOUND)
        );
        return club.getHost().equals(member);
    }

    private void addClubMember(Member member, Club club) {
        // ?????? ?????? ?????? ??????
        if (club.getNum() == club.getMax())
            throw new ClubException(ClubErrorCode.CLUB_FULL_MEMBER);
        // ?????? ?????? ??????
        if (clubMemberRepository.findByMemberAndClub_Id(member, club.getId()).isPresent())
            throw new ClubException(ClubErrorCode.CLUB_DUPLICATED_MEMBER);
        // ?????? ?????? ???????????? ??????
        ClubMember clubMember = new ClubMember(club, member);
        member.getClubs().add(clubMember);
        club.getMembers().add(clubMember);
        club.setNum(club.getNum() + 1);
        clubMemberRepository.save(clubMember);
    }


}
