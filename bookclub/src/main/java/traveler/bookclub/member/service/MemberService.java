package traveler.bookclub.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import traveler.bookclub.auth.dto.GoogleProfile;
import traveler.bookclub.auth.dto.TokenDto;
import traveler.bookclub.auth.service.AuthService;
import traveler.bookclub.member.domain.Member;
import traveler.bookclub.member.domain.Role;
import traveler.bookclub.member.exception.MemberException;
import traveler.bookclub.member.repository.MemberRepository;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public String test() {
        Member currentMember = findCurrentMember();
        return "인증정보="+currentMember.getUsername();
    }

    public Member findCurrentMember() {
        // TODO: 쿼리 날리지 않고 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member user = memberRepository.findByUsername(authentication.getName()).get();
        return user;
    }

    @Transactional
    public Member findMemberByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberException());
    }

    @Transactional
    public boolean verifyMember(String username) {
        return memberRepository.findByUsername(username).isPresent();
    }

    @Transactional
    public Member createMember(GoogleProfile profile) {
        Member newUser = Member.builder()
                .nickname(profile.getName())
                .email(profile.getEmail())
                .password(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("social1234"))
                .profileUrl(profile.getImgUrl())
                .username(profile.getId())
                .build();

        return memberRepository.save(newUser);
    }


    @Transactional
    public void updateMember(Member member, GoogleProfile profile) {
        //
    }
}
