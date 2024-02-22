package com.lesso.neverland.post.presentation;

import com.lesso.neverland.common.BaseException;
import com.lesso.neverland.common.BaseResponse;
import com.lesso.neverland.post.application.PostService;
import com.lesso.neverland.post.dto.ModifyPostViewResponse;
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
    @PatchMapping("/{postIdx}/editView")
    public BaseResponse<ModifyPostViewResponse> getModifyPostView(@PathVariable Long postIdx) {
        try {
            return new BaseResponse<>(postService.getModifyPostView(postIdx));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // [작성자] 피드 삭제
    @PatchMapping("/{postIdx}/delete")
    public BaseResponse<?> deletePost(@PathVariable Long postIdx) {
        try {
            postService.deletePost(postIdx);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
