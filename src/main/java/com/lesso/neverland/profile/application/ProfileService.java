package com.lesso.neverland.profile.application;

import com.lesso.neverland.common.BaseException;
import com.lesso.neverland.common.image.ImageService;
import com.lesso.neverland.follow.repository.FollowRepository;
import com.lesso.neverland.profile.domain.Thumbnail;
import com.lesso.neverland.profile.dto.*;
import com.lesso.neverland.user.application.UserService;
import com.lesso.neverland.user.domain.User;
import com.lesso.neverland.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static com.lesso.neverland.common.BaseResponseStatus.*;
import static com.lesso.neverland.common.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
public class ProfileService {

    UserRepository userRepository;
    UserService userService;
    FollowRepository followRepository;
    ImageService imageService;

    // 프로필 조회
    public ProfileResponse getProfile(GetProfileRequest getProfileRequest) throws BaseException {
        try {
            User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));

            Integer followingNumber = followRepository.countByFollowingUser(user);
            Integer followerNumber = followRepository.countByFollowedUser(user);

            List<Thumbnail> thumbnails = user.getThumbnails();
            List<ThumbnailDto> thumbnailList = thumbnails.stream()
                    .map(thumbnail -> new ThumbnailDto(
                            thumbnail.getThumbnailOrder().getOrder(),
                            thumbnail.getImageUrl())).collect(Collectors.toList());

            return new ProfileResponse(user.getProfile().getNickname(), user.getProfile().getProfileImage(), user.getProfile().getProfileImage(), user.getProfile().getProfileMusic(),
                    user.getProfile().getProfileMusicUrl(), followingNumber, followerNumber, isMyProfile(user, getProfileRequest.nickname()), thumbnailList);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 유저가 해당 프로필의 주인인지 체크
    private boolean isMyProfile(User user, String nickname) {
        User profileUser = userRepository.findByProfile_Nickname(nickname);
        return user.equals(profileUser);
    }

    // [유저] 프로필 수정 화면 조회
    public ProfileModifyViewResponse getProfileModifyView() throws BaseException {
        try {
            User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            return new ProfileModifyViewResponse(user.getProfile().getProfileImage(), user.getProfile().getNickname(), user.getProfile().getProfileMessage(),
                    user.getProfile().getProfileMusic(), user.getProfile().getProfileMusicUrl());
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [유저] 프로필 수정
    @Transactional(rollbackFor = Exception.class)
    public void editProfile(MultipartFile image, EditProfileRequest editProfileRequest) throws BaseException {
        try {
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
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
