package com.lesso.neverland.post.presentation;

import com.lesso.neverland.common.BaseException;
import com.lesso.neverland.common.BaseResponse;
import com.lesso.neverland.post.application.PostService;
import com.lesso.neverland.post.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
