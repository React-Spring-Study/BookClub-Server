package traveler.bookclub.clubMember;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import traveler.bookclub.club.dto.ClubInfoResponse;

//TODO: 없애기
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubMemberTest {
    private Long id;
    private ClubInfoResponse club;
    private String nickname;

    public static ClubMemberTest toDto(ClubMember clubMember) {
        return new ClubMemberTest(
                clubMember.getId(),
                ClubInfoResponse.of(clubMember.getClub()),
                clubMember.getMember().getNickname()
        );
    }
}
