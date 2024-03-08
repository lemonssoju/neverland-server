package com.lesso.neverland.profile.presentation;

import com.lesso.neverland.common.BaseException;
import com.lesso.neverland.common.BaseResponse;
import com.lesso.neverland.profile.application.ProfileService;
import com.lesso.neverland.profile.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.lesso.neverland.common.BaseResponseStatus.SUCCESS;
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

    // [유저] 프로필 수정 화면 조회
    @GetMapping("/editView")
    public BaseResponse<ProfileEditViewResponse> getProfileEditView() {
        try {
            return new BaseResponse<>(profileService.getProfileEditView());
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // [유저] 프로필 수정
    @PatchMapping("/edit")
    public BaseResponse<String> editProfile(@RequestPart MultipartFile image, @RequestPart EditProfileRequest editProfileRequest) {
        try {
            profileService.editProfile(image, editProfileRequest);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 대표 사진(Thumbnail) 등록
    @PostMapping("/thumbnail")
    public BaseResponse<String> postThumbnail(@RequestPart MultipartFile image, @RequestPart PostThumbnailRequest postThumbnailRequest) {
        try {
            profileService.postThumbnail(image, postThumbnailRequest);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
