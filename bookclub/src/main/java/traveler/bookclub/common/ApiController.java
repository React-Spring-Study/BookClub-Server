package traveler.bookclub.common;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import traveler.bookclub.auth.dto.AuthInfo;
import traveler.bookclub.auth.dto.TokenDto;
import traveler.bookclub.auth.service.AuthService;
import traveler.bookclub.common.response.StringResponse;
import traveler.bookclub.member.service.MemberService;

@RestController
@RequiredArgsConstructor
public class ApiController {

    private final AuthService authService;
    private final MemberService memberService;

    @PostMapping("/test-login1")
    public TokenDto testLogin() {
        AuthInfo info = authService.testLogin1();
        return new TokenDto(info.getMemberId(), info.getAccessToken().getToken(), info.getMemberRefreshToken().getRefreshToken());
    }

    @PostMapping("/test-login2")
    public TokenDto testLoginV2() {
        AuthInfo info = authService.testLogin2();
        return new TokenDto(info.getMemberId(), info.getAccessToken().getToken(), info.getMemberRefreshToken().getRefreshToken());
    }

    @GetMapping("/test-request")
    public StringResponse testRequest() {
        return new StringResponse("username: " + memberService.test());
    }

    @GetMapping("/health")
    public StringResponse healthCheck() {
        return new StringResponse("Health Check v2");
    }
}
