package traveler.bookclub.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import traveler.bookclub.auth.dto.AuthInfo;
import traveler.bookclub.auth.dto.LoginRequest;
import traveler.bookclub.auth.dto.TokenDto;
import traveler.bookclub.auth.service.AuthService;

@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class MemberController {

    private final AuthService authService;

    @PostMapping("/join")
    public TokenDto join(@RequestBody LoginRequest request) {
        AuthInfo authInfo = authService.signUp(request.getToken());
        return new TokenDto(authInfo.getAccessToken().getToken(), authInfo.getMemberRefreshToken().getRefreshToken());
    }

    @PostMapping("/login")
    public TokenDto login(@RequestBody LoginRequest request) {
        AuthInfo authInfo = authService.login(request.getToken());
        return new TokenDto(authInfo.getAccessToken().getToken(), authInfo.getMemberRefreshToken().getRefreshToken());
    }

    @PostMapping("/reissue")
    public TokenDto reissue(@RequestBody TokenDto request){
        AuthInfo info = authService.reissueToken(request);
        return new TokenDto(info.getAccessToken().getToken(), info.getMemberRefreshToken().getRefreshToken());
    }
}
