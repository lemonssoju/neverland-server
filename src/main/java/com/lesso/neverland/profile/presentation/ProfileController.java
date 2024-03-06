package com.lesso.neverland.profile.presentation;

import com.lesso.neverland.common.BaseException;
import com.lesso.neverland.common.BaseResponse;
import com.lesso.neverland.profile.application.ProfileService;
import com.lesso.neverland.profile.dto.GetProfileRequest;
import com.lesso.neverland.profile.dto.ProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.lesso.neverland.common.constants.RequestURI.profile;

@RestController
@RequiredArgsConstructor
@RequestMapping(profile)
public class ProfileController {

    ProfileService profileService;

    // 프로필 조회
    @GetMapping("")
    public BaseResponse<ProfileResponse> getProfile(@RequestBody GetProfileRequest getProfileRequest) {
        try {
            return new BaseResponse<>(profileService.getProfile(getProfileRequest));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

}
