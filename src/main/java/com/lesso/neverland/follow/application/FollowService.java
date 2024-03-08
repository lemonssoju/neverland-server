package com.lesso.neverland.follow.application;

import com.lesso.neverland.common.BaseException;
import com.lesso.neverland.follow.dto.FollowDto;
import com.lesso.neverland.follow.dto.FollowListResponse;
import com.lesso.neverland.follow.repository.FollowRepository;
import com.lesso.neverland.user.application.UserService;
import com.lesso.neverland.user.domain.User;
import com.lesso.neverland.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.lesso.neverland.common.BaseResponseStatus.DATABASE_ERROR;
import static com.lesso.neverland.common.BaseResponseStatus.INVALID_USER_IDX;
import static com.lesso.neverland.common.constants.Constants.ACTIVE;

@Service
@RequiredArgsConstructor
public class FollowService {

    UserRepository userRepository;
    UserService userService;
    FollowRepository followRepository;

    // 팔로우 목록 조회(following, follower)
    public FollowListResponse getFollowList() throws BaseException {
        try {
            User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));

            List<FollowDto> followingList = followRepository.findByFollowingUserAndStatusEquals(user, ACTIVE).stream()
                    .map(follow -> new FollowDto(
                            follow.getFollowedUser().getProfile().getNickname(),
                            follow.getFollowedUser().getProfile().getProfileImage())).toList();
            List<FollowDto> followedList = followRepository.findByFollowedUserAndStatusEquals(user, ACTIVE).stream()
                    .map(follow -> new FollowDto(
                            follow.getFollowingUser().getProfile().getNickname(),
                            follow.getFollowingUser().getProfile().getProfileImage())).toList();
            return new FollowListResponse(followingList, followedList);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
