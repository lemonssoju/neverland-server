package com.lesso.neverland.comment.presentation;

import com.lesso.neverland.comment.application.CommentService;
import com.lesso.neverland.comment.dto.ModifyCommentRequest;
import com.lesso.neverland.comment.dto.PostCommentRequest;
import com.lesso.neverland.common.BaseException;
import com.lesso.neverland.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.lesso.neverland.common.BaseResponseStatus.SUCCESS;
import static com.lesso.neverland.common.constants.RequestURI.comment;

@RestController
@RequiredArgsConstructor
@RequestMapping(comment)
public class CommentController {
    private final CommentService commentService;

    // 댓글 등록
    @PostMapping("")
    public BaseResponse<String> postComment(@RequestBody PostCommentRequest commentPostRequest) {
        try {
            commentService.postComment(commentPostRequest);
            return new BaseResponse<>(SUCCESS);
        } catch(BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // [작성자] 댓글 수정
    @PatchMapping("/{commentIdx}")
    public BaseResponse<String> modifyComment(@PathVariable Long commentIdx, @RequestBody ModifyCommentRequest modifyCommentRequest) {
        try {
            commentService.modifyComment(commentIdx, modifyCommentRequest);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("/{commentIdx}/delete")
    public BaseResponse<?> deleteComment(@PathVariable Long commentIdx) {
        try {
            commentService.deleteComment(commentIdx);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
