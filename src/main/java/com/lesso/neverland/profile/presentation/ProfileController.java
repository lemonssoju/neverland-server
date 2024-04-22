package com.lesso.neverland.profile.presentation;

import com.lesso.neverland.common.base.BaseResponse;
import com.lesso.neverland.profile.application.ProfileService;
import com.lesso.neverland.profile.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.lesso.neverland.common.constants.RequestURI.profile;

@RestController
@RequiredArgsConstructor
@RequestMapping(profile)
public class ProfileController {

    ProfileService profileService;

    // 프로필 조회
    @GetMapping("")
    public BaseResponse<ProfileResponse> getProfile(@RequestBody GetProfileRequest getProfileRequest) {
        return profileService.getProfile(getProfileRequest);
    }

    // [유저] 프로필 수정 화면 조회
    @GetMapping("/editView")
    public BaseResponse<ProfileEditViewResponse> getProfileEditView() {
        return profileService.getProfileEditView();
    }

    // [유저] 프로필 수정
    @PatchMapping("/edit")
    public BaseResponse<String> editProfile(@RequestPart MultipartFile image, @RequestPart EditProfileRequest editProfileRequest) {
        try {
            return profileService.editProfile(image, editProfileRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 대표 사진(Thumbnail) 등록 및 수정
    @PostMapping("/thumbnail")
    public BaseResponse<String> postThumbnail(@RequestPart MultipartFile image, @RequestPart PostThumbnailRequest postThumbnailRequest) {
        try {
            return profileService.postThumbnail(image, postThumbnailRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
