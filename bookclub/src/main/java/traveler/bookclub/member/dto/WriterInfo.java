package traveler.bookclub.member.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import traveler.bookclub.member.domain.Member;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WriterInfo {

    private String nickname;
    private String imgUrl;

    public static WriterInfo toDto(Member member) {
        return new WriterInfo(member.getNickname(), member.getProfileUrl());
    }
}
