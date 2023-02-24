package traveler.bookclub.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import traveler.bookclub.auth.dto.AuthInfo;
import traveler.bookclub.auth.dto.JoinRequest;
import traveler.bookclub.auth.dto.LoginRequest;
import traveler.bookclub.auth.dto.TokenDto;
import traveler.bookclub.auth.service.AuthService;
import traveler.bookclub.common.response.StringResponse;
import traveler.bookclub.member.dto.MemberInfoResponse;
import traveler.bookclub.member.dto.MemberUpdateDto;
import traveler.bookclub.member.service.MemberService;

@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
public class MemberController {

    private final AuthService authService;
    private final MemberService memberService;

    @PostMapping("/join")
    public TokenDto join(@Valid @RequestBody JoinRequest request) {
        AuthInfo authInfo = authService.signUp(request);
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

    @PutMapping("/me")
    public StringResponse changeNickname(@Valid @RequestBody MemberUpdateDto form) {
        String newName = memberService.updateMyName(form);
        return new StringResponse("닉네임을 성공적으로 수정하였습니다. 변경된 닉네임: " + newName);
    }
}
