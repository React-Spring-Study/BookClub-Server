package traveler.bookclub.member.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import traveler.bookclub.member.domain.Member;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInfoResponse {
    private String nickname;
    private String email;
    private String profileUrl;

    public static MemberInfoResponse toResponse(Member member) {
        return new MemberInfoResponse(
                member.getNickname(),
                member.getEmail(),
                member.getProfileUrl()
        );
    }
}
