package com.lesso.neverland.profile.application;

import com.lesso.neverland.common.BaseException;
import com.lesso.neverland.follow.repository.FollowRepository;
import com.lesso.neverland.profile.domain.Thumbnail;
import com.lesso.neverland.profile.dto.GetProfileRequest;
import com.lesso.neverland.profile.dto.ProfileModifyViewResponse;
import com.lesso.neverland.profile.dto.ProfileResponse;
import com.lesso.neverland.profile.dto.ThumbnailDto;
import com.lesso.neverland.user.application.UserService;
import com.lesso.neverland.user.domain.User;
import com.lesso.neverland.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
