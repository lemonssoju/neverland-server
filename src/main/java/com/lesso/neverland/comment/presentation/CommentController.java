package com.lesso.neverland.comment.presentation;

import com.lesso.neverland.comment.application.CommentService;
import com.lesso.neverland.comment.dto.ModifyCommentRequest;
import com.lesso.neverland.comment.dto.PostCommentRequest;
import com.lesso.neverland.common.base.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.lesso.neverland.common.constants.RequestURI.comment;

@RestController
@RequiredArgsConstructor
@RequestMapping(comment)
public class CommentController {
    private final CommentService commentService;

    // 댓글 등록
    @PostMapping("")
    public BaseResponse<String> postComment(@RequestBody PostCommentRequest commentPostRequest) {
        return commentService.postComment(commentPostRequest);
    }

    // [작성자] 댓글 수정
    @PatchMapping("/{commentIdx}")
    public BaseResponse<String> modifyComment(@PathVariable Long commentIdx, @RequestBody ModifyCommentRequest modifyCommentRequest) {
        return commentService.modifyComment(commentIdx, modifyCommentRequest);
    }

    // [작성자] 댓글 삭제
    @PatchMapping("/{commentIdx}/delete")
    public BaseResponse<?> deleteComment(@PathVariable Long commentIdx) {
        return commentService.deleteComment(commentIdx);
    }
}
