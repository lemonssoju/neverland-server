package com.lesso.neverland.user.presentation;

import com.lesso.neverland.common.BaseException;
import com.lesso.neverland.common.BaseResponse;
import com.lesso.neverland.user.application.AuthService;
import com.lesso.neverland.user.application.UserService;
import com.lesso.neverland.user.dto.SignupRequest;
import com.lesso.neverland.user.dto.SignupResponse;
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
    public BaseResponse<SignupResponse> signup(@RequestBody SignupRequest signupRequest) {
        try {
            return new BaseResponse<>(userService.signup(signupRequest));
        } catch(BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
