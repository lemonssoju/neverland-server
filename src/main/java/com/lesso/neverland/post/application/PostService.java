package com.lesso.neverland.post.application;

import com.lesso.neverland.comment.domain.Comment;
import com.lesso.neverland.comment.dto.CommentDto;
import com.lesso.neverland.common.BaseException;
import com.lesso.neverland.common.enums.Contents;
import com.lesso.neverland.post.domain.Post;
import com.lesso.neverland.post.domain.PostLike;
import com.lesso.neverland.post.domain.PostTag;
import com.lesso.neverland.post.dto.ModifyPostViewResponse;
import com.lesso.neverland.post.dto.PostResponse;
import com.lesso.neverland.post.dto.RecommendedPostDto;
import com.lesso.neverland.post.repository.PostLikeRepository;
import com.lesso.neverland.post.repository.PostRepository;
import com.lesso.neverland.user.application.AuthService;
import com.lesso.neverland.user.application.UserService;
import com.lesso.neverland.user.domain.User;
import com.lesso.neverland.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.lesso.neverland.common.BaseResponseStatus.*;
import static com.lesso.neverland.common.constants.Constants.ACTIVE;
import static com.lesso.neverland.common.constants.Constants.INACTIVE;

@Service
@RequiredArgsConstructor
public class PostService {

    private final AuthService authService;
    private final UserService userService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;

    // 피드 상세 조회
    public PostResponse getPost(Long postIdx) throws BaseException {
        try {
            Post post = postRepository.findById(postIdx).orElseThrow(() -> new BaseException(INVALID_POST_IDX));
            if (post.getStatus().equals(INACTIVE)) throw new BaseException(ALREADY_DELETED_POST);

            // 좋아요 여부
            Optional<Long> optionalUserIdx = Optional.ofNullable(authService.getUserIdx());
            boolean isLike = false;
            if (optionalUserIdx.isPresent()) { // 로그인 상태일 경우에만 좋아요 여부 및 추천글 조회
                User user = userRepository.findById(optionalUserIdx.get()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
                isLike = postLikeRepository.existsByPostAndUserAndStatusEquals(post, user, ACTIVE);
            }

            // 추천글 목록 구성
            List<Contents> postTags = post.getPostTags().stream()
                    .map(PostTag::getTagName).collect(Collectors.toList());

            List<Post> recommendedPosts = postRepository.findTop3ByPostTags_TagNameIn(postTags);
            List<RecommendedPostDto> posts = recommendedPosts.stream()
                    .map(recommendedPost -> new RecommendedPostDto(
                            recommendedPost.getPostImage(),
                            recommendedPost.getTitle(),
                            recommendedPost.getPostTags().stream()
                                    .map(postTag -> postTag.getTagName().getName())
                                    .collect(Collectors.toList()))).collect(Collectors.toList());

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

            return new PostResponse(post.getTitle(), post.getSubtitle(), post.getContent(), post.getCreatedDate(),
                    post.getUser().getProfile().getNickname(), post.getBackgroundMusic(), post.getBackgroundMusicUrl(),
                    post.getPostImage(), isLike, posts, comments);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [작성자] 피드 수정 화면 조회
    public ModifyPostViewResponse getModifyPostView(Long postIdx) throws BaseException {
        try {
            User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            Post post = postRepository.findById(postIdx).orElseThrow(() -> new BaseException(INVALID_POST_IDX));
            validateWriter(user, post);

            return new ModifyPostViewResponse(post.getTitle(), post.getSubtitle(), post.getContentsType().getName(),
                    post.getBackgroundMusic(), post.getBackgroundMusicUrl(), post.getPostImage(), post.getContent());
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [작성자] 피드 삭제
    @Transactional(rollbackFor = Exception.class)
    public void deletePost(Long postIdx) throws BaseException {
        try {
            User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            Post post = postRepository.findById(postIdx).orElseThrow(() -> new BaseException(INVALID_POST_IDX));
            validateWriter(user, post);

            post.delete();
            postRepository.save(post);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 좋아요/취소
    @Transactional(rollbackFor = Exception.class)
    public void likePost(Long postIdx) throws BaseException {
        try {
            User user = userRepository.findById(authService.getUserIdx()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            Post post = postRepository.findById(postIdx).orElseThrow(() -> new BaseException(INVALID_POST_IDX));
            PostLike postLike = postLikeRepository.findByPostAndUser(post, user);

            if (postLike == null) { // 이 게시글에 대한 해당 유저의 첫 좋아요
                postLike = PostLike.builder()
                        .post(post)
                        .user(user).build();
                postLike.setPost(post);
                postLike.setUser(user);
            } else { // 좋아요 기록이 이미 존재할 경우
                if (postLike.getStatus().equals(ACTIVE)) { // 좋아요 상태면 취소
                    postLike.delete();
                } else { // 취소 상태면 좋아요
                    postLike.add();
                }
            }
            postLikeRepository.save(postLike);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 작성자 validation
    private static void validateWriter(User user, Post post) throws BaseException {
        if (!post.getUser().equals(user)) throw new BaseException(NO_POST_WRITER);
        if (post.getStatus().equals(INACTIVE)) throw new BaseException(ALREADY_DELETED_POST);
    }
}