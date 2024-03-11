package com.lesso.neverland.post.presentation;

import com.lesso.neverland.common.BaseException;
import com.lesso.neverland.common.BaseResponse;
import com.lesso.neverland.post.application.PostService;
import com.lesso.neverland.post.dto.MyLikeListResponse;
import com.lesso.neverland.post.dto.PostEditViewResponse;
import com.lesso.neverland.post.dto.MyPostListResponse;
import com.lesso.neverland.post.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.lesso.neverland.common.BaseResponseStatus.SUCCESS;
import static com.lesso.neverland.common.constants.RequestURI.post;

@RestController
@RequiredArgsConstructor
@RequestMapping(post)
public class PostController {
    private final PostService postService;

    // 피드 상세 조회
    @GetMapping("/{postIdx}")
    public BaseResponse<PostResponse> getPost(@PathVariable Long postIdx) {
        try {
            return new BaseResponse<>(postService.getPost(postIdx));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // [작성자] 피드 수정 화면 조회
    @GetMapping("/{postIdx}/editView")
    public BaseResponse<PostEditViewResponse> getPostEditView(@PathVariable Long postIdx) {
        try {
            return new BaseResponse<>(postService.getPostEditView(postIdx));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // [작성자] 피드 삭제
    @PatchMapping("/{postIdx}/delete")
    public BaseResponse<String> deletePost(@PathVariable Long postIdx) {
        try {
            postService.deletePost(postIdx);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 좋아요/취소
    @PostMapping("/{postIdx}")
    public BaseResponse<String> likePost(@PathVariable("postIdx") Long postIdx) {
        try {
            postService.likePost(postIdx);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // [유저] 작성한 글 목록 조회
    @GetMapping("/myPosts")
    public BaseResponse<MyPostListResponse> getMyPostList() {
        try {
            return new BaseResponse<>(postService.getMyPostList());
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // [유저] 졸아요한 글 목록 조회
    @GetMapping("/myLikes")
    public BaseResponse<MyLikeListResponse> getMyLikeList() {
        try {
            return new BaseResponse<>(postService.getMyLikeList());
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
