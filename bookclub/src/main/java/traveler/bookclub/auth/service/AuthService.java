package traveler.bookclub.auth.service;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import traveler.bookclub.auth.domain.AuthToken;
import traveler.bookclub.auth.domain.AuthTokenProvider;
import traveler.bookclub.auth.domain.CustomUserDetails;
import traveler.bookclub.auth.domain.MemberRefreshToken;
import traveler.bookclub.auth.dto.AuthInfo;
import traveler.bookclub.auth.dto.GoogleProfile;
import traveler.bookclub.auth.dto.JoinRequest;
import traveler.bookclub.auth.dto.TokenDto;
import traveler.bookclub.auth.exception.AuthErrorCode;
import traveler.bookclub.auth.exception.AuthException;
import traveler.bookclub.auth.repository.MemberRefreshTokenRepository;
import traveler.bookclub.common.util.AppProperties;
import traveler.bookclub.member.domain.Member;
import traveler.bookclub.member.domain.Role;
import traveler.bookclub.member.exception.MemberErrorCode;
import traveler.bookclub.member.exception.MemberException;
import traveler.bookclub.member.repository.MemberRepository;
import traveler.bookclub.member.service.MemberService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;
    private final Gson gson;
    private final AuthTokenProvider tokenProvider;
    private final MemberRefreshTokenRepository refreshTokenRepository;
    private final AppProperties appProperties;
    private final AuthenticationManager authenticationManager;
    private static final long THREE_DAYS_MSEC = 259200000;
    private static final String SOCIAL_PW = "social1234";

    @Transactional
    public AuthInfo testLogin1() {
        GoogleProfile googleProfile = new GoogleProfile(
                "namjihyunTest", "?????????", "helloworld@gmail.com", "http://s3lksdfasdflkds"
        );
        memberService.createMember(googleProfile);
        return new AuthInfo(1L, createAuth(googleProfile), setRefreshToken(googleProfile));
    }

    @Transactional
    public AuthInfo testLogin2() {
        GoogleProfile googleProfile = new GoogleProfile(
                "springTest", "?????????", "helloSpring@naver.com", "http://s3dfrhhyjsdgawfefsd"
        );
        memberService.createMember(googleProfile);
        return new AuthInfo(2L,createAuth(googleProfile), setRefreshToken(googleProfile));
    }

    @Transactional
    public AuthInfo signUp(JoinRequest request) {
        GoogleProfile profile = joinProcess(request.getNickname(), getProfileByToken(request.getToken()));
        return new AuthInfo(profile.getMemberId(), createAuth(profile), setRefreshToken(profile));
    }

    @Transactional
    public AuthInfo login(String token) {
        GoogleProfile profile = loginProcess(getProfileByToken(token));
        return new AuthInfo(profile.getMemberId(), createAuth(profile), setRefreshToken(profile));
    }


    private Map getProfileByToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        String url = "https://www.googleapis.com/oauth2/v2/userinfo";

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return gson.fromJson(response.getBody(), HashMap.class);
            }
        } catch (Exception e) {
            log.error(e.toString());
            throw new AuthException(AuthErrorCode.GOOGLE_SERVER_FAILED);
        }
        throw new AuthException(AuthErrorCode.GOOGLE_SERVER_FAILED);
    }

    @Transactional
    public AuthInfo reissueToken(TokenDto dto) {
        AuthToken expiredToken = tokenProvider.convertAuthToken(dto.getAccessToken());
        AuthToken refreshToken = tokenProvider.convertAuthToken(dto.getRefreshToken());

        Claims claims = expiredToken.getExpiredTokenClaims();
        if (claims == null) {
            throw new AuthException(AuthErrorCode.NOT_EXPIRED_TOKEN_YET);
        } else {
            log.info("claims={}", claims);
        }
        String username = claims.getSubject();
        Role role = Role.of(claims.get("role", String.class));

        if (!refreshToken.validate()) {
            throw new AuthException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }

        // refresh token?????? DB?????? user ????????? ??????
        MemberRefreshToken memberRefreshToken = refreshTokenRepository.findByUsernameAndRefreshToken(username, dto.getRefreshToken());
        log.info("UserRefreshToken={}", refreshToken);
        if (memberRefreshToken == null) {
            throw new AuthException( AuthErrorCode.INVALID_REFRESH_TOKEN,
                    "???????????? ?????? ??????????????? ???????????? ?????? ???????????? ???????????????."
            );
        }


        Date now = new Date();

        AuthToken newAccessToken = tokenProvider.createAuthToken(
                username,
                role.getName(),
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );

        long validTime = refreshToken.getTokenClaims().getExpiration().getTime() - now.getTime();

        // ?????? ??????????????? 3??? ????????? ?????? refresh token ??????
        if (validTime <= THREE_DAYS_MSEC) {
            long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

            refreshToken = tokenProvider.createAuthToken(
                    appProperties.getAuth().getTokenSecret(),
                    new Date(now.getTime() + refreshTokenExpiry)
            );
            // DB??? ?????? ????????????
            memberRefreshToken.setRefreshToken(refreshToken.getToken());
        }

        return new AuthInfo(dto.getMemberId(), newAccessToken, memberRefreshToken);
    }

    private GoogleProfile joinProcess(String nickname, Map<String, Object> attributes) {

        if (memberService.verifyMember((String) attributes.get("id")))
            throw new MemberException(MemberErrorCode.MEMBER_DUPLICATED); // ?????? ????????? ?????? ???????????? ??????

        GoogleProfile profile = GoogleProfile.toProfile(attributes); // ???????????? ????????? ???????????? ?????? ??????
        profile.setName(nickname!=null? nickname : profile.getName()); // ???????????? ????????? ??????????????? ??????

        if (memberRepository.findByUsername(profile.getId()).isEmpty()) {
            Long memberId = memberService.createMember(profile);
            profile.setMemberId(memberId);
        }
        return profile;
    }

    private GoogleProfile loginProcess(Map<String, Object> attributes) {
        // profile??? ??????. ???????????? ????????? ???????????? ?????? ??????
        GoogleProfile profile = GoogleProfile.toProfile(attributes);

        // ?????? ???????????? ??????
        Member existedMember = memberService.findMemberByUsername(profile.getId());

        // ?????? ??????????????? ??????
        profile.setName(existedMember.getNickname());
        memberService.updateMember(existedMember, profile);
        profile.setMemberId(existedMember.getId());

        return profile;
    }

    private AuthToken createAuth(GoogleProfile profile) {

        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            profile.getId(),
                            SOCIAL_PW
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            Date now = new Date();
            String username = profile.getId();

            AuthToken accessToken = tokenProvider.createAuthToken(
                    username,
                    ((CustomUserDetails) authentication.getPrincipal()).getRole().getName(),
                    new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
            );

            return accessToken;
        } catch (BadCredentialsException e) {
            throw new AuthException(AuthErrorCode.CREDENTIAL_MISS_MATCH);
        }
    }

    // ???????????? ?????? ?????? ??? ?????? ?????? ??????
    private MemberRefreshToken setRefreshToken(GoogleProfile profile) {

        Date now = new Date();
        String username = profile.getId();

        long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();
        AuthToken refreshToken = tokenProvider.createAuthToken(
                appProperties.getAuth().getTokenSecret(),
                new Date(now.getTime() + refreshTokenExpiry)
        );

        //userId refresh token ?????? DB ??????
        MemberRefreshToken memberRefreshToken = refreshTokenRepository.findByUsername(username);
        if (memberRefreshToken == null) {
            // ????????? ?????? ??????
            memberRefreshToken = new MemberRefreshToken(username, refreshToken.getToken());
            refreshTokenRepository.save(memberRefreshToken);
        } else {
            // DB??? refresh token ????????????
            memberRefreshToken.setRefreshToken(refreshToken.getToken());
        }

        return memberRefreshToken;
    }
}
