package com.lesso.neverland.group.application;

import com.lesso.neverland.comment.domain.Comment;
import com.lesso.neverland.comment.dto.CommentDto;
import com.lesso.neverland.comment.repository.CommentRepository;
import com.lesso.neverland.common.base.BaseException;
import com.lesso.neverland.common.base.BaseResponse;
import com.lesso.neverland.common.enums.Contents;
import com.lesso.neverland.common.enums.Source;
import com.lesso.neverland.common.image.ImageService;
import com.lesso.neverland.group.domain.Team;
import com.lesso.neverland.group.dto.*;
import com.lesso.neverland.group.repository.GroupRepository;
import com.lesso.neverland.post.domain.Post;
import com.lesso.neverland.post.domain.PostLike;
import com.lesso.neverland.post.domain.PostTag;
import com.lesso.neverland.post.repository.PostLikeRepository;
import com.lesso.neverland.post.repository.PostRepository;
import com.lesso.neverland.post.repository.PostTagRepository;
import com.lesso.neverland.user.application.AuthService;
import com.lesso.neverland.user.application.UserService;
import com.lesso.neverland.user.domain.User;
import com.lesso.neverland.user.domain.UserTeam;
import com.lesso.neverland.user.repository.UserRepository;
import com.lesso.neverland.user.repository.UserTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.lesso.neverland.common.base.BaseResponseStatus.*;
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
    private final ImageService imageService;
    private final UserTeamRepository userTeamRepository;
    private final CommentRepository commentRepository;
    private final PostTagRepository postTagRepository;

    // 그룹 목록 조회
    public BaseResponse<GroupListResponse> getGroupList() {
        User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        List<Team> groupList = groupRepository.findByAdminAndStatusEquals(user, ACTIVE);
        List<GroupListDto> groupListDto = groupList.stream()
                .map(group -> new GroupListDto(
                        group.getTeamIdx(),
                        group.getTeamImage(),
                        group.getName())).collect(Collectors.toList());
        return new BaseResponse<>(new GroupListResponse(groupListDto));
    }

    // 그룹 피드 목록 조회
    public BaseResponse<GroupPostListResponse> getGroupPostList(Long groupIdx) {
        User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        Team group = groupRepository.findById(groupIdx).orElseThrow(() -> new BaseException(INVALID_GROUP_IDX));
        List<Post> groupPostList = postRepository.findByTeamAndStatusEqualsOrderByCreatedDateDesc(group, ACTIVE);

        List<GroupPostDto> groupPostListDto = groupPostList.stream()
                .map(groupPost -> new GroupPostDto(
                        groupPost.getTitle(),
                        groupPost.getSubtitle(),
                        groupPost.getPostImage(),
                        groupPost.getUser().getProfile().getNickname())).collect(Collectors.toList());
        return new BaseResponse<>(new GroupPostListResponse(group.getName(), groupPostListDto));
    }

    // 그룹 피드 상세 조회
    public BaseResponse<GroupPostResponse> getGroupPost(Long groupIdx, Long postIdx) {
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

        return new BaseResponse<>(new GroupPostResponse(post.getTitle(), post.getSubtitle(), post.getContent(), post.getCreatedDate(),
                post.getUser().getProfile().getNickname(), post.getBackgroundMusic(), post.getBackgroundMusicUrl(),
                post.getPostImage(), isLike, comments));
    }

    // [관리자] 그룹 수정 화면 조회
    public BaseResponse<GroupEditViewResponse> getGroupEditView(Long groupIdx) {
        Team group = groupRepository.findById(groupIdx).orElseThrow(() -> new BaseException(INVALID_GROUP_IDX));
        User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        validateAdmin(user, group);

        return new BaseResponse<>(new GroupEditViewResponse(group.getName(), group.getTeamImage()));
    }

    // [관리자] 그룹 수정
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<String> editGroup(Long groupIdx, MultipartFile image, EditGroupRequest editGroupRequest) throws IOException {
        Team group = groupRepository.findById(groupIdx).orElseThrow(() -> new BaseException(INVALID_GROUP_IDX));
        User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        validateAdmin(user, group);

        if (editGroupRequest.name() != null) {
            if (!editGroupRequest.name().equals("") && !editGroupRequest.name().equals(" "))
                group.modifyName(editGroupRequest.name());
            else throw new BaseException(BLANK_GROUP_NAME);
        } if (editGroupRequest.memberList() != null) {
            List<UserTeam> originalMemberList = group.getUserTeams();
            userTeamRepository.deleteAll(originalMemberList);

            for(Long userIdx : editGroupRequest.memberList()) {
                User member = userRepository.findById(userIdx).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
                UserTeam userTeam = UserTeam.builder()
                        .user(member)
                        .team(group).build();
                userTeam.setUser(member);
                userTeam.setTeam(group);
                userTeamRepository.save(userTeam);
            }
        }

        if (image != null) {//TODO: 이미지 삭제 및 업로드 설정 후 동작 확인하기
            // delete previous image
            imageService.deleteImage(group.getTeamImage());

            // upload new image
            String imagePath = imageService.uploadImage("group", image);
            group.modifyImage(imagePath);
        } else throw new BaseException(NULL_GROUP_IMAGE);
        groupRepository.save(group);
        return new BaseResponse<>(SUCCESS);
    }

    // [관리자] 그룹 삭제
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<String> deleteGroup(Long groupIdx) {
        Team group = groupRepository.findById(groupIdx).orElseThrow(() -> new BaseException(INVALID_GROUP_IDX));
        User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        validateAdmin(user, group);

        List<Post> groupPostList = postRepository.findByTeamAndStatusEquals(group, ACTIVE);
        List<Comment> commentsSaveList = new ArrayList<>();
        List<PostLike> postLikesSaveList = new ArrayList<>();
        List<PostTag> postTagsSaveList = new ArrayList<>();
        List<UserTeam> userTeamsSaveList = new ArrayList<>();

        for (Post post : groupPostList) {
            post.delete();
            for(Comment comment : post.getComments()) {
                comment.delete();
                commentsSaveList.add(comment);
            }
            for(PostLike postLike : post.getPostLikes()) {
                postLike.delete();
                postLikesSaveList.add(postLike);
            }
            for(PostTag postTag : post.getPostTags()) {
                postTag.delete();
                postTagsSaveList.add(postTag);
            }
        }
        for (UserTeam userTeam : group.getUserTeams()) {
            userTeam.delete();
            userTeamsSaveList.add(userTeam);
        }
        postRepository.saveAll(groupPostList);
        commentRepository.saveAll(commentsSaveList);
        postLikeRepository.saveAll(postLikesSaveList);
        postTagRepository.saveAll(postTagsSaveList);
        userTeamRepository.saveAll(userTeamsSaveList);

        group.delete();
        groupRepository.save(group);
        return new BaseResponse<>(SUCCESS);
    }

    // 그룹 나가기
    public BaseResponse<String> withdrawGroup(Long groupIdx) {
        Team group = groupRepository.findById(groupIdx).orElseThrow(() -> new BaseException(INVALID_GROUP_IDX));
        User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        if(user.equals(group.getAdmin())) throw new BaseException(GROUP_ADMIN);

        UserTeam userTeam = userTeamRepository.findByUserAndTeam(user, group);
        if (userTeam == null) throw new BaseException(NO_GROUP_MEMBER);
        else {
            userTeam.delete();
            userTeamRepository.save(userTeam);
        }
        return new BaseResponse<>(SUCCESS);
    }

    // 그룹 생성
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<String> createGroup(MultipartFile image, CreateGroupRequest createGroupRequest) throws IOException {
        User admin = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));

        // upload image
        String imagePath = imageService.uploadImage("group", image);

        Team group = new Team(admin, createGroupRequest.name(), imagePath);
        groupRepository.save(group);

        UserTeam newUserTeam = new UserTeam(admin, group);
        newUserTeam.setUser(admin);
        newUserTeam.setTeam(group);
        userTeamRepository.save(newUserTeam);

        for (Long memberIdx : createGroupRequest.memberList()) {
            User member = userRepository.findById(memberIdx).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            UserTeam userTeam = new UserTeam(member, group);
            userTeam.setUser(member);
            userTeam.setTeam(group);
            userTeamRepository.save(userTeam);
        }
        return new BaseResponse<>(SUCCESS);
    }

    // 그룹 피드 등록
    public BaseResponse<String> createGroupPost(Long groupIdx, MultipartFile image, GroupPostRequest groupPostRequest) throws IOException {
        Team group = groupRepository.findById(groupIdx).orElseThrow(() -> new BaseException(INVALID_GROUP_IDX));
        User writer = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));

        // upload image
        String imagePath = imageService.uploadImage("group", image);

        Contents contentsType = Contents.getEnumByName(groupPostRequest.contentsType());
        // TODO: gpt 활용한 태그 생성
        Post post = new Post(writer, Source.USER, group, groupPostRequest.title(), groupPostRequest.subtitle(),
                contentsType, groupPostRequest.backgroundMusic(), groupPostRequest.backgroundMusicUrl(), imagePath, groupPostRequest.content());
        postRepository.save(post);
        return new BaseResponse<>(SUCCESS);
    }

    private void validateAdmin(User user, Team group) {
        if (!group.getAdmin().equals(user)) throw new BaseException(NO_GROUP_ADMIN);
    }
}
