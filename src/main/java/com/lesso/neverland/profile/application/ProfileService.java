package com.lesso.neverland.profile.application;

import com.lesso.neverland.common.base.BaseException;
import com.lesso.neverland.common.base.BaseResponse;
import com.lesso.neverland.common.enums.ThumbnailOrder;
import com.lesso.neverland.common.image.ImageService;
import com.lesso.neverland.follow.repository.FollowRepository;
import com.lesso.neverland.profile.domain.Thumbnail;
import com.lesso.neverland.profile.dto.*;
import com.lesso.neverland.profile.repository.ThumbnailRepository;
import com.lesso.neverland.user.application.UserService;
import com.lesso.neverland.user.domain.User;
import com.lesso.neverland.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.lesso.neverland.common.base.BaseResponseStatus.*;
import static com.lesso.neverland.common.constants.Constants.ACTIVE;

@Service
@RequiredArgsConstructor
public class ProfileService {

    UserRepository userRepository;
    UserService userService;
    FollowRepository followRepository;
    ImageService imageService;
    ThumbnailRepository thumbnailRepository;

    // 프로필 조회
    public BaseResponse<ProfileResponse> getProfile(GetProfileRequest getProfileRequest) {
        User profileOwner = userRepository.findById(getProfileRequest.userIdx()).orElseThrow(() -> new BaseException(NO_MATCH_USER));
        User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));

        Integer followingNumber = followRepository.countByFollowingUser(profileOwner);
        Integer followerNumber = followRepository.countByFollowedUser(profileOwner);

        List<Thumbnail> thumbnails = profileOwner.getThumbnails();
        List<ThumbnailDto> thumbnailList = thumbnails.stream()
                .map(thumbnail -> new ThumbnailDto(
                        thumbnail.getThumbnailOrder().getOrder(),
                        thumbnail.getImageUrl())).collect(Collectors.toList());

        return new BaseResponse<>(new ProfileResponse(profileOwner.getUserIdx(), profileOwner.getProfile().getNickname(), profileOwner.getProfile().getProfileImage(), profileOwner.getProfile().getProfileMessage(),
                profileOwner.getProfile().getProfileMusic(), profileOwner.getProfile().getProfileMusicUrl(), followingNumber, followerNumber, isMyProfile(profileOwner, user), thumbnailList));
    }

    // 유저가 해당 프로필의 주인인지 체크
    private boolean isMyProfile(User profileOwner, User user) {
        return profileOwner.equals(user);
    }

    // [유저] 프로필 수정 화면 조회
    public BaseResponse<ProfileEditViewResponse> getProfileEditView() {
        User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        return new BaseResponse<>(new ProfileEditViewResponse(user.getProfile().getProfileImage(), user.getProfile().getNickname(), user.getProfile().getProfileMessage(),
                user.getProfile().getProfileMusic(), user.getProfile().getProfileMusicUrl()));
    }

    // [유저] 프로필 수정
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<String> editProfile(MultipartFile image, EditProfileRequest editProfileRequest) throws IOException {
        User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));

        if (editProfileRequest.nickname() != null) { // nickname만 null 불가능
            if (!editProfileRequest.nickname().equals("") && !editProfileRequest.nickname().equals(" "))
                user.getProfile().modifyNickname(editProfileRequest.nickname());
            else throw new BaseException(BLANK_NICKNAME);
        }
        user.getProfile().modifyProfileMessage(editProfileRequest.profileMessage());
        user.getProfile().modifyProfileMusic(editProfileRequest.profileMusic());
        user.getProfile().modifyProfileMusicUrl(editProfileRequest.profileMusicUrl());

        // TODO: 이미지 삭제 및 업로드 설정 후 동작 확인하기
        // delete previous image
        imageService.deleteImage(user.getProfile().getProfileImage());
        if (image != null) {
            // upload new image
            String imagePath = imageService.uploadImage("profile", image);
            user.getProfile().modifyProfileImage(imagePath);
        }
        userRepository.save(user);
        return new BaseResponse<>(SUCCESS);
    }

    // 대표 사진(Thumbnail) 등록 및 수정
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<String> postThumbnail(MultipartFile image, PostThumbnailRequest postThumbnailRequest) throws IOException {
        User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));

        // TODO: 동작 확인 필요
        thumbnailRepository.findByUserAndThumbnailOrderAndStatusEquals(user, ThumbnailOrder.valueOfOrder(postThumbnailRequest.order()), ACTIVE)
                .ifPresent(originalThumbnail -> imageService.deleteImage(originalThumbnail.getImageUrl()));

        // upload image
        String imagePath = imageService.uploadImage("thumbnail", image);
        Thumbnail thumbnail = new Thumbnail(user, ThumbnailOrder.valueOfOrder(postThumbnailRequest.order()), imagePath);

        thumbnailRepository.save(thumbnail);
        thumbnail.setUser(user);
        return new BaseResponse<>(SUCCESS);
    }
}
