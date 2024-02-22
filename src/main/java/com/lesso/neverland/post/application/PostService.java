package com.lesso.neverland.post.application;

import com.lesso.neverland.comment.domain.Comment;
import com.lesso.neverland.common.BaseException;
import com.lesso.neverland.common.enums.Contents;
import com.lesso.neverland.interest.domain.Interest;
import com.lesso.neverland.post.domain.Post;
import com.lesso.neverland.post.dto.ModifyPostViewResponse;
import com.lesso.neverland.post.dto.PostResponse;
import com.lesso.neverland.post.repository.PostLikeRepository;
import com.lesso.neverland.post.repository.PostRepository;
import com.lesso.neverland.user.application.AuthService;
import com.lesso.neverland.user.domain.User;
import com.lesso.neverland.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.lesso.neverland.common.BaseResponseStatus.*;
import static com.lesso.neverland.common.constants.Constants.ACTIVE;
import static com.lesso.neverland.common.constants.Constants.INACTIVE;

@Service
@RequiredArgsConstructor
public class PostService {

    private final AuthService authService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;

    // 피드 상세 조회
    public PostResponse getPost(Long postIdx) throws BaseException {
        try {
            User user = userRepository.findById(authService.getUserIdx()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            Post post = postRepository.findById(postIdx).orElseThrow(() -> new BaseException(INVALID_POST_IDX));
            if (post.getStatus().equals(INACTIVE)) throw new BaseException(ALREADY_DELETED_POST);

            boolean isLike = postLikeRepository.existsByPostAndUserAndStatusEquals(post, user, ACTIVE);

            // 사용자의 취향 태그 조회
            List<Contents> userInterests = user.getInterests().stream()
                    .map(Interest::getPreference)
                    .collect(Collectors.toList());

            // 추천글 조회
            List<Post> recommendedPosts = postRepository.findTop3ByPostTags_TagNameIn(userInterests);
            List<PostResponse.RecommendedPostDto> posts = recommendedPosts.stream()
                    .map(recommendedPost -> new PostResponse.RecommendedPostDto(
                            recommendedPost.getTitle(),
                            recommendedPost.getPostTags().stream()
                                    .map(postTag -> postTag.getTagName().getName())
                                    .collect(Collectors.toList())))
                    .collect(Collectors.toList());

            // 댓글 조회
            List<Comment> postComments = post.getComments();
            List<PostResponse.CommentDto> comments = postComments.stream()
                    .map(comment -> new PostResponse.CommentDto(
                            comment.getUser().getProfile().getNickname(),
                            comment.getUser().getProfile().getProfileImage(),
                            comment.getCreatedDate(),
                            comment.getContent()))
                    .collect(Collectors.toList());

            return new PostResponse(post.getTitle(), post.getSubtitle(), post.getContent(), post.getCreatedDate(),
                    post.getUser().getProfile().getNickname(), user.getProfile().getNickname(), post.getBackgroundMusic(), post.getBackgroundMusicUrl(),
                    post.getPostImage(), isLike, posts, comments);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [작성자] 피드 수정 화면 조회
    public ModifyPostViewResponse getModifyPostView(Long postIdx) throws BaseException {
        User user = userRepository.findById(getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        Post post = postRepository.findById(postIdx).orElseThrow(() -> new BaseException(INVALID_POST_IDX));
        validateWriter(user, post);

        return new ModifyPostViewResponse(post.getTitle(), post.getSubtitle(), post.getContentsType().getName(),
                post.getBackgroundMusic(), post.getBackgroundMusicUrl(), post.getPostImage(), post.getContent());
    }

    // 회원만
    private Long getUserIdxWithValidation() throws BaseException {
        Long userIdx = authService.getUserIdx();
        if (userIdx == null) throw new BaseException(NULL_ACCESS_TOKEN);
        return userIdx;
    }

    // 작성자 validation
    private static void validateWriter(User user, Post post) throws BaseException {
        if (!post.getUser().equals(user)) throw new BaseException(NO_POST_WRITER);
        if (post.getStatus().equals(INACTIVE)) throw new BaseException(ALREADY_DELETED_POST);
    }
}
