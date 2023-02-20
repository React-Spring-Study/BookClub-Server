package traveler.bookclub.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import traveler.bookclub.auth.dto.AuthInfo;
import traveler.bookclub.auth.dto.LoginRequest;
import traveler.bookclub.auth.dto.TokenDto;
import traveler.bookclub.auth.service.AuthService;
import traveler.bookclub.member.dto.MemberInfoResponse;
import traveler.bookclub.member.service.MemberService;

@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class MemberController {

    private final AuthService authService;
    private final MemberService memberService;

    @PostMapping("/join")
    public TokenDto join(@Valid @RequestBody LoginRequest request) {
        AuthInfo authInfo = authService.signUp(request.getToken());
        return new TokenDto(authInfo.getAccessToken().getToken(), authInfo.getMemberRefreshToken().getRefreshToken());
    }

    @PostMapping("/login")
    public TokenDto login(@Valid @RequestBody LoginRequest request) {
        AuthInfo authInfo = authService.login(request.getToken());
        return new TokenDto(authInfo.getAccessToken().getToken(), authInfo.getMemberRefreshToken().getRefreshToken());
    }

    @PostMapping("/reissue")
    public TokenDto reissue(@Valid @RequestBody TokenDto request){
        AuthInfo info = authService.reissueToken(request);
        return new TokenDto(info.getAccessToken().getToken(), info.getMemberRefreshToken().getRefreshToken());
    }

    @GetMapping("/me")
    public MemberInfoResponse readMe() {
        return memberService.showMe();
    }
}
