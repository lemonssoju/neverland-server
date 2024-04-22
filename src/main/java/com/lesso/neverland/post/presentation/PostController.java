package com.lesso.neverland.post.presentation;

import com.lesso.neverland.common.base.BaseResponse;
import com.lesso.neverland.post.application.PostService;
import com.lesso.neverland.post.dto.MyLikeListResponse;
import com.lesso.neverland.post.dto.PostEditViewResponse;
import com.lesso.neverland.post.dto.MyPostListResponse;
import com.lesso.neverland.post.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.lesso.neverland.common.constants.RequestURI.post;

@RestController
@RequiredArgsConstructor
@RequestMapping(post)
public class PostController {
    private final PostService postService;

    // 피드 상세 조회
    @GetMapping("/{postIdx}")
    public BaseResponse<PostResponse> getPost(@PathVariable Long postIdx) {
        return postService.getPost(postIdx);
    }

    // [작성자] 피드 수정 화면 조회
    @GetMapping("/{postIdx}/editView")
    public BaseResponse<PostEditViewResponse> getPostEditView(@PathVariable Long postIdx) {
        return postService.getPostEditView(postIdx);
    }

    // [작성자] 피드 삭제
    @PatchMapping("/{postIdx}/delete")
    public BaseResponse<String> deletePost(@PathVariable Long postIdx) {
        return postService.deletePost(postIdx);
    }

    // 좋아요/취소
    @PostMapping("/{postIdx}")
    public BaseResponse<String> likePost(@PathVariable("postIdx") Long postIdx) {
        return postService.likePost(postIdx);
    }

    // [유저] 작성한 글 목록 조회
    @GetMapping("/myPosts")
    public BaseResponse<MyPostListResponse> getMyPostList() {
        return postService.getMyPostList();
    }

    // [유저] 졸아요한 글 목록 조회
    @GetMapping("/myLikes")
    public BaseResponse<MyLikeListResponse> getMyLikeList() {
        return postService.getMyLikeList();
    }
}
