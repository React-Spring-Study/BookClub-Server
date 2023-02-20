package traveler.bookclub.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import traveler.bookclub.auth.domain.CustomUserDetails;
import traveler.bookclub.member.domain.Member;
import traveler.bookclub.member.exception.MemberErrorCode;
import traveler.bookclub.member.exception.MemberException;
import traveler.bookclub.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        return CustomUserDetails.create(member);
    }
}
