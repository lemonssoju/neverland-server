package com.lesso.neverland.user.presentation;

import com.lesso.neverland.common.base.BaseResponse;
import com.lesso.neverland.user.application.AuthService;
import com.lesso.neverland.user.application.UserService;
import com.lesso.neverland.user.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.lesso.neverland.common.constants.RequestURI.user;

@RestController
@RequiredArgsConstructor
@RequestMapping(user)
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    // 회원가입
    @PostMapping(value = "/signup")
    public BaseResponse<JwtDto> signup(@RequestBody SignupRequest signupRequest) {
        return userService.signup(signupRequest);
    }

    // 로그인
    @PostMapping("/login")
    public BaseResponse<JwtDto> login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    // 로그아웃
    @PatchMapping("/logout")
    public BaseResponse<String> logout() {
        return userService.logout(authService.getUserIdx());
    }

    // 회원 탈퇴
    @PatchMapping("/signout")
    public BaseResponse<String> signOut(@RequestBody SignoutRequest signoutRequest) {
        return userService.signout(authService.getUserIdx(), signoutRequest);
    }

    // access token 재발급
    @PostMapping("/reissue-token")
    public BaseResponse<TokenResponse> reissueToken(@RequestBody ReissueTokenRequest reissueTokenRequest) {
        return userService.reissueAccessToken(reissueTokenRequest);
    }

    // 닉네임 중복 체크
    @PostMapping("/nickname")
    public BaseResponse<String> validateNickname(@RequestBody NicknameRequest nicknameRequest) {
        return userService.validateNickname(nicknameRequest.nickname());
    }

    // 아이디 중복 체크
    @PostMapping("/loginId")
    public BaseResponse<String> validateLoginId(@RequestBody LoginIdRequest loginIdRequest) {
        return userService.validateLoginId(loginIdRequest.loginId());
    }

    // 개인 정보 수정
    @PatchMapping("/modify")
    public BaseResponse<String> modifyUser(@RequestBody ModifyUserRequest modifyUserRequest) {
        return userService.modifyUser(authService.getUserIdx(), modifyUserRequest);
    }
}
