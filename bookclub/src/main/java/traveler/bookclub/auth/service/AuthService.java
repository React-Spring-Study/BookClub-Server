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
                "namjihyunTest", "남지현", "helloworld@gmail.com", "http://s3lksdfasdflkds"
        );
        memberService.createMember(googleProfile);
        return new AuthInfo(1L, createAuth(googleProfile), setRefreshToken(googleProfile));
    }

    @Transactional
    public AuthInfo testLogin2() {
        GoogleProfile googleProfile = new GoogleProfile(
                "springTest", "스프링", "helloSpring@naver.com", "http://s3dfrhhyjsdgawfefsd"
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

        // refresh token으로 DB에서 user 정보와 확인
        MemberRefreshToken memberRefreshToken = refreshTokenRepository.findByUsernameAndRefreshToken(username, dto.getRefreshToken());
        log.info("UserRefreshToken={}", refreshToken);
        if (memberRefreshToken == null) {
            throw new AuthException( AuthErrorCode.INVALID_REFRESH_TOKEN,
                    "가입되지 않은 회원이거나 유효하지 않은 리프레시 토큰입니다."
            );
        }


        Date now = new Date();

        AuthToken newAccessToken = tokenProvider.createAuthToken(
                username,
                role.getName(),
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );

        long validTime = refreshToken.getTokenClaims().getExpiration().getTime() - now.getTime();

        // 토큰 만료기간이 3일 이하인 경우 refresh token 발급
        if (validTime <= THREE_DAYS_MSEC) {
            long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

            refreshToken = tokenProvider.createAuthToken(
                    appProperties.getAuth().getTokenSecret(),
                    new Date(now.getTime() + refreshTokenExpiry)
            );
            // DB에 토큰 업데이트
            memberRefreshToken.setRefreshToken(refreshToken.getToken());
        }

        return new AuthInfo(dto.getMemberId(), newAccessToken, memberRefreshToken);
    }

    private GoogleProfile joinProcess(String nickname, Map<String, Object> attributes) {

        if (memberService.verifyMember((String) attributes.get("id")))
            throw new MemberException(MemberErrorCode.MEMBER_DUPLICATED); // 이미 가입된 멤버 회원가입 불가

        GoogleProfile profile = GoogleProfile.toProfile(attributes); // 구글에서 받아온 이름으로 우선 지정
        profile.setName(nickname!=null? nickname : profile.getName()); // 사용자가 지정한 닉네임으로 수정

        if (memberRepository.findByUsername(profile.getId()).isEmpty()) {
            Long memberId = memberService.createMember(profile);
            profile.setMemberId(memberId);
        }
        return profile;
    }

    private GoogleProfile loginProcess(Map<String, Object> attributes) {
        // profile로 변환. 구글에서 받아온 이름으로 우선 지정
        GoogleProfile profile = GoogleProfile.toProfile(attributes);

        // 기존 회원인지 확인
        Member existedMember = memberService.findMemberByUsername(profile.getId());

        // 기존 닉네임으로 수정
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

    // 리프레시 토큰 발급 및 저장 또는 수정
    private MemberRefreshToken setRefreshToken(GoogleProfile profile) {

        Date now = new Date();
        String username = profile.getId();

        long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();
        AuthToken refreshToken = tokenProvider.createAuthToken(
                appProperties.getAuth().getTokenSecret(),
                new Date(now.getTime() + refreshTokenExpiry)
        );

        //userId refresh token 으로 DB 확인
        MemberRefreshToken memberRefreshToken = refreshTokenRepository.findByUsername(username);
        if (memberRefreshToken == null) {
            // 없으면 새로 등록
            memberRefreshToken = new MemberRefreshToken(username, refreshToken.getToken());
            refreshTokenRepository.save(memberRefreshToken);
        } else {
            // DB에 refresh token 업데이트
            memberRefreshToken.setRefreshToken(refreshToken.getToken());
        }

        return memberRefreshToken;
    }
}
