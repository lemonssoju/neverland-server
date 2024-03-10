package com.lesso.neverland.follow.application;

import com.lesso.neverland.common.BaseException;
import com.lesso.neverland.follow.domain.Follow;
import com.lesso.neverland.follow.dto.FollowDto;
import com.lesso.neverland.follow.dto.FollowListResponse;
import com.lesso.neverland.follow.dto.FollowRequest;
import com.lesso.neverland.follow.repository.FollowRepository;

import com.lesso.neverland.profile.dto.MemberInviteDto;
import com.lesso.neverland.profile.dto.MemberInviteListResponse;
import com.lesso.neverland.user.application.UserService;
import com.lesso.neverland.user.domain.User;
import com.lesso.neverland.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.lesso.neverland.common.BaseResponseStatus.*;
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
                            follow.getFollowedUser().getUserIdx(),
                            follow.getFollowedUser().getProfile().getNickname(),
                            follow.getFollowedUser().getProfile().getProfileImage())).toList();
            List<FollowDto> followedList = followRepository.findByFollowedUserAndStatusEquals(user, ACTIVE).stream()
                    .map(follow -> new FollowDto(
                            follow.getFollowingUser().getUserIdx(),
                            follow.getFollowingUser().getProfile().getNickname(),
                            follow.getFollowingUser().getProfile().getProfileImage())).toList();
            return new FollowListResponse(followingList, followedList);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 맞팔로우 목록 조회
    public MemberInviteListResponse getMemberInviteList() throws BaseException {
        try {
            User writer = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));

            List<MemberInviteDto> memberInviteList = followRepository.findByFollowingUserAndStatusEquals(writer, ACTIVE).stream()
                    .filter(follow -> followRepository.findByFollowingUserAndFollowedUserAndStatusEquals(follow.getFollowedUser(), writer, ACTIVE).isPresent())
                    .map(follow -> new MemberInviteDto(
                            follow.getFollowedUser().getProfile().getNickname(),
                            follow.getFollowedUser().getProfile().getProfileImage())).toList();
            return new MemberInviteListResponse(memberInviteList);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 팔로우/취소
    public void follow(FollowRequest followRequest) throws BaseException {
        try {
            User followingUser = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            User followedUser = userRepository.findById(followRequest.userIdx()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));

            Optional<Follow> optionalFollow = followRepository.findByFollowingUserAndFollowedUserAndStatusEquals(followingUser, followedUser, ACTIVE);

            Follow follow;
            if (optionalFollow.isPresent()) { // 팔로우 기록이 이미 존재할 경우
                follow = optionalFollow.get();
                if (follow.getStatus().equals(ACTIVE)) { // 팔로우 상태면 언팔로우
                    follow.unfollow();
                } else { // 언팔로우 상태면 팔로우
                    follow.follow();
                }
            } else { // 첫 팔로우
                follow = new Follow(followingUser, followedUser);
            }
            followRepository.save(follow);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
