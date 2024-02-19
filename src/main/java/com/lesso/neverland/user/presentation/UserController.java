package com.lesso.neverland.user.presentation;

import com.lesso.neverland.common.BaseException;
import com.lesso.neverland.common.BaseResponse;
import com.lesso.neverland.user.application.AuthService;
import com.lesso.neverland.user.application.UserService;
import com.lesso.neverland.user.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.lesso.neverland.common.BaseResponseStatus.SUCCESS;
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
        try {
            return new BaseResponse<>(userService.signup(signupRequest));
        } catch(BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 로그인
    @PostMapping("/login")
    public BaseResponse<JwtDto> login(@RequestBody LoginRequest loginRequest) {
        try {
            return new BaseResponse<>(userService.login(loginRequest));
        } catch(BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 로그아웃
    @PatchMapping("/logout")
    public BaseResponse<?> logout(@RequestBody TokenRequest tokenRequest) {
        try{
            userService.logout(authService.getUserIdx(), tokenRequest.refreshToken());
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 회원 탈퇴
    @PatchMapping("/signout")
    public BaseResponse<?> signOut(@RequestBody TokenRequest tokenRequest) {
        try{
            userService.signout(authService.getUserIdx(), tokenRequest.refreshToken());
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // access token 재발급
    @PostMapping("/reissue-token")
    public BaseResponse<TokenResponse> reissueToken(@RequestBody ReissueTokenRequest reissueTokenRequest) {
        try{
            return new BaseResponse<>(userService.reissueAccessToken(reissueTokenRequest));
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
