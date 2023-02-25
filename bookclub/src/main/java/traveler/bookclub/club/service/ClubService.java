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
    public void updateClub(ClubUpdateRequest request, MultipartFile img) {
        if (! verifyClubHost(memberService.findCurrentMember(), request.getClubId())) // 호스트 권한 확인
            throw new ClubException(ClubErrorCode.CLUB_NO_AUTH);
        Club club = clubRepository.findById(request.getClubId()).orElseThrow(
                () -> new ClubException(ClubErrorCode.CLUB_NOT_FOUND)
        );

        // 최대 인원이 현재 인원보다 작지 않은지 확인
        if (request.getMax() < club.getNum())
            throw new ClubException(ClubErrorCode.CLUB_MAX_TOO_SMALL);

        String url = club.getImgUrl();

        // 대표 이미지 수정
        if (! img.isEmpty()) {
            // 일단 입력이 있으면 업로드. 기존 이미지 있어도 overwrite
            club.setImgUrl(s3Service.uploadClubImage(club.getId(), img));
        } else if (url!=null) {
            // 입력이 없는데 기존 이미지가 있었던 경우 -> 이미지 삭제
            s3Service.deleteImage(url);
            club.setImgUrl(null);
        }

        club.updateClub(request.getName(), request.getInformation(), request.getMax(), request.getLink());

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


}
