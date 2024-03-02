package com.lesso.neverland.group.application;

import com.lesso.neverland.comment.domain.Comment;
import com.lesso.neverland.comment.dto.CommentDto;
import com.lesso.neverland.common.BaseException;
import com.lesso.neverland.group.domain.Team;
import com.lesso.neverland.group.dto.*;
import com.lesso.neverland.group.repository.GroupRepository;
import com.lesso.neverland.post.domain.Post;
import com.lesso.neverland.post.repository.PostLikeRepository;
import com.lesso.neverland.post.repository.PostRepository;
import com.lesso.neverland.user.application.AuthService;
import com.lesso.neverland.user.application.UserService;
import com.lesso.neverland.user.domain.User;
import com.lesso.neverland.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.lesso.neverland.common.BaseResponseStatus.*;
import static com.lesso.neverland.common.constants.Constants.ACTIVE;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final PostRepository postRepository;
    private final AuthService authService;
    private final PostLikeRepository postLikeRepository;

    // 그룹 목록 조회
    public GroupListResponse getGroupList() throws BaseException {
        try {
            User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            List<Team> groupList = groupRepository.findByAdminAndStatusEquals(user, ACTIVE);
            List<GroupListDto> groupListDto = groupList.stream()
                    .map(group -> new GroupListDto(
                            group.getTeamIdx(),
                            group.getTeamImage(),
                            group.getName(),
                            group.getSubName())).collect(Collectors.toList());
            return new GroupListResponse(groupListDto);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 그룹 피드 목록 조회
    public GroupPostListResponse getGroupPostList(Long groupIdx) throws BaseException {
        try {
            User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            Team group = groupRepository.findById(groupIdx).orElseThrow(() -> new BaseException(INVALID_GROUP_IDX));
            List<Post> groupPostList = postRepository.findByTeamAndStatusEqualsOrderByCreatedDateDesc(group, ACTIVE);

            List<GroupPostDto> groupPostListDto = groupPostList.stream()
                    .map(groupPost -> new GroupPostDto(
                            groupPost.getTitle(),
                            groupPost.getSubtitle(),
                            groupPost.getPostImage(),
                            groupPost.getUser().getProfile().getNickname())).collect(Collectors.toList());
            return new GroupPostListResponse(group.getName(), groupPostListDto);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 그룹 피드 상세 조회
    public GroupPostResponse getGroupPost(Long groupIdx, Long postIdx) throws BaseException {
        try {
            Team group = groupRepository.findById(groupIdx).orElseThrow(() -> new BaseException(INVALID_GROUP_IDX));
            Post post = postRepository.findById(postIdx).orElseThrow(() -> new BaseException(INVALID_POST_IDX));
            if (!post.getTeam().equals(group)) throw new BaseException(NO_GROUP_POST);

            // 좋아요 여부 조회
            Optional<Long> optionalUserIdx = Optional.ofNullable(authService.getUserIdx());
            boolean isLike = false;
            if (optionalUserIdx.isPresent()){
                User user = userRepository.findById(optionalUserIdx.get()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
                isLike = postLikeRepository.existsByPostAndUserAndStatusEquals(post, user, ACTIVE);
            }

            // 댓글 조회
            List<Comment> postComments = post.getComments();
            List<CommentDto> comments = postComments.stream()
                    .map(comment -> new CommentDto(
                            comment.getCommentIdx(),
                            comment.getUser().getProfile().getNickname(),
                            comment.getUser().getProfile().getProfileImage(),
                            comment.getCreatedDate(),
                            comment.getContent()))
                    .collect(Collectors.toList());

            return new GroupPostResponse(post.getTitle(), post.getSubtitle(), post.getContent(), post.getCreatedDate(),
                    post.getUser().getProfile().getNickname(), post.getBackgroundMusic(), post.getBackgroundMusicUrl(),
                    post.getPostImage(), isLike, comments);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
