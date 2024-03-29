package com.lesso.neverland.comment.application;

import com.lesso.neverland.comment.domain.Comment;
import com.lesso.neverland.comment.dto.ModifyCommentRequest;
import com.lesso.neverland.comment.dto.PostCommentRequest;
import com.lesso.neverland.comment.repository.CommentRepository;
import com.lesso.neverland.common.BaseException;
import com.lesso.neverland.common.BaseResponse;
import com.lesso.neverland.post.domain.Post;
import com.lesso.neverland.post.repository.PostRepository;
import com.lesso.neverland.user.application.UserService;
import com.lesso.neverland.user.domain.User;
import com.lesso.neverland.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.lesso.neverland.common.BaseResponseStatus.*;
import static com.lesso.neverland.common.constants.Constants.INACTIVE;

@Service
@RequiredArgsConstructor
public class CommentService {
    UserRepository userRepository;
    CommentRepository commentRepository;
    PostRepository postRepository;
    UserService userService;

    // 댓글 등록
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<String> postComment(PostCommentRequest postCommentRequest) {
        User writer = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        Post post = postRepository.findById(postCommentRequest.postIdx()).orElseThrow(() -> new BaseException(INVALID_POST_IDX));

        Comment comment = new Comment(post, writer, postCommentRequest.content());
        comment.setPost(post);
        comment.setUser(writer);
        commentRepository.save(comment);
        return new BaseResponse<>(SUCCESS);
    }

    // [작성자] 댓글 수정
    public BaseResponse<String> modifyComment(Long commentIdx, ModifyCommentRequest modifyCommentRequest) {
        User writer = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        Comment comment = commentRepository.findById(commentIdx).orElseThrow(() -> new BaseException(INVALID_COMMENT_IDX));
        validateWriter(writer, comment);

        if (modifyCommentRequest.content() != null) {
            if (!modifyCommentRequest.content().equals("") && !modifyCommentRequest.content().equals(" "))
                comment.modifyContent(modifyCommentRequest.content());
            else throw new BaseException(BLANK_COMMENT_CONTENT);
        }
        commentRepository.save(comment);
        return new BaseResponse<>(SUCCESS);
    }

    // [작성자] 댓글 삭제
    public BaseResponse<String> deleteComment(Long commentIdx) {
        User writer = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        Comment comment = commentRepository.findById(commentIdx).orElseThrow(() -> new BaseException(INVALID_COMMENT_IDX));
        validateWriter(writer, comment);

        comment.delete();
        commentRepository.save(comment);
        return new BaseResponse<>(SUCCESS);
    }

    // 작성자 validation
    private static void validateWriter(User user, Comment comment) {
        if (!comment.getUser().equals(user)) throw new BaseException(NO_COMMENT_WRITER);
        if (comment.getStatus().equals(INACTIVE)) throw new BaseException(ALREADY_DELETED_COMMENT);
    }
}
