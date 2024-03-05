package com.lesso.neverland.comment.presentation;

import com.lesso.neverland.comment.application.CommentService;
import com.lesso.neverland.comment.dto.PostCommentRequest;
import com.lesso.neverland.common.BaseException;
import com.lesso.neverland.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.lesso.neverland.common.BaseResponseStatus.SUCCESS;
import static com.lesso.neverland.common.constants.RequestURI.comment;

@RestController
@RequiredArgsConstructor
@RequestMapping(comment)
public class CommentController {
    private final CommentService commentService;

    @PostMapping("")
    public BaseResponse<?> postComment(@RequestBody PostCommentRequest commentPostRequest) {
        try {
            commentService.postComment(commentPostRequest);
            return new BaseResponse<>(SUCCESS);
        } catch(BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
