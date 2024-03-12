package com.lesso.neverland.follow.application;

import com.lesso.neverland.common.BaseException;
import com.lesso.neverland.follow.domain.Follow;
import com.lesso.neverland.follow.dto.*;
import com.lesso.neverland.follow.repository.FollowRepository;

import com.lesso.neverland.group.domain.Team;
import com.lesso.neverland.group.repository.GroupRepository;
import com.lesso.neverland.profile.dto.MemberInviteDto;
import com.lesso.neverland.profile.dto.MemberInviteListResponse;
import com.lesso.neverland.user.application.UserService;
import com.lesso.neverland.user.domain.User;
import com.lesso.neverland.user.repository.UserRepository;
import com.lesso.neverland.user.repository.UserTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.lesso.neverland.common.BaseResponseStatus.*;
import static com.lesso.neverland.common.constants.Constants.ACTIVE;

@Service
@RequiredArgsConstructor
public class FollowService {

    UserRepository userRepository;
    UserService userService;
    FollowRepository followRepository;
    GroupRepository groupRepository;
    UserTeamRepository userTeamRepository;

    // 팔로잉 목록 조회
    public FollowingListResponse getFollowingList(String searchWord) throws BaseException {
        try {
            User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));

            List<FollowDto> followingList = Optional.ofNullable(searchWord)
                    .map(search -> followRepository.findByFollowingUserAndFollowedNickname(user, search))
                    .orElseGet(() -> followRepository.findByFollowingUserAndStatusEquals(user, ACTIVE))
                    .stream()
                    .map(this::convertToFollowDto).toList();
            return new FollowingListResponse(followingList);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 팔로워 목록 조회
    public FollowerListResponse getFollowerList(String searchWord) throws BaseException {
        try {
            User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));

            List<FollowDto> followerList = Optional.ofNullable(searchWord)
                    .map(search -> followRepository.findByFollowedUserAndFollowingNickname(user, search))
                    .orElseGet(() -> followRepository.findByFollowedUserAndStatusEquals(user, ACTIVE))
                    .stream()
                    .map(this::convertToFollowDto).toList();
            return new FollowerListResponse(followerList);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // FollowDto로 가공
    private FollowDto convertToFollowDto(Follow follow) {
        return new FollowDto(
                follow.getFollowedUser().getUserIdx(),
                follow.getFollowedUser().getProfile().getNickname(),
                follow.getFollowedUser().getProfile().getProfileImage());
    }

    // [그룹 생성] 맞팔로우 목록 조회
    public MemberInviteListResponse getMemberInviteList(String searchWord) throws BaseException {
        try {
            User writer = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            return new MemberInviteListResponse(getMemberInviteDtoList(writer, follow -> false, searchWord));
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [그룹 수정] 맞팔로우 목록 조회
    public MemberInviteListResponse getMemberInviteListEditView(Long groupIdx, String searchWord) throws BaseException {
        try {
            User writer = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            Team group = groupRepository.findById(groupIdx).orElseThrow(() -> new BaseException(INVALID_GROUP_IDX));

            return new MemberInviteListResponse(getMemberInviteDtoList(writer, follow -> userTeamRepository.existsByUserAndTeam(follow.getFollowedUser(), group), searchWord));
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private List<MemberInviteDto> getMemberInviteDtoList(User writer, Function<Follow, Boolean> isMemberFunction, String searchWord) {
        List<Follow> follows = Optional.ofNullable(searchWord)
                .map(search -> followRepository.findByFollowingUserAndFollowedNickname(writer, search))
                .orElseGet(() -> followRepository.findByFollowingUserAndStatusEquals(writer, ACTIVE));

        return follows.stream()
                .filter(follow -> followRepository.findByFollowingUserAndFollowedUserAndStatusEquals(follow.getFollowedUser(), writer, ACTIVE).isPresent())
                .map(follow -> new MemberInviteDto(
                        follow.getFollowedUser().getProfile().getNickname(),
                        follow.getFollowedUser().getProfile().getProfileImage(),
                        isMemberFunction.apply(follow))).toList();
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
