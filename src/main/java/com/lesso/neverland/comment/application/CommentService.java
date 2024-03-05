package com.lesso.neverland.comment.application;

import com.lesso.neverland.comment.domain.Comment;
import com.lesso.neverland.comment.dto.PostCommentRequest;
import com.lesso.neverland.comment.repository.CommentRepository;
import com.lesso.neverland.common.BaseException;
import com.lesso.neverland.post.domain.Post;
import com.lesso.neverland.post.repository.PostRepository;
import com.lesso.neverland.user.application.UserService;
import com.lesso.neverland.user.domain.User;
import com.lesso.neverland.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.lesso.neverland.common.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class CommentService {
    UserRepository userRepository;
    CommentRepository commentRepository;
    PostRepository postRepository;
    UserService userService;

    // 댓글 등록
    @Transactional(rollbackFor = Exception.class)
    public void postComment(PostCommentRequest postCommentRequest) throws BaseException {
        try {
            User writer = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            Post post = postRepository.findById(postCommentRequest.postIdx()).orElseThrow(() -> new BaseException(INVALID_POST_IDX));

            Comment comment = new Comment(post, writer, postCommentRequest.content());
            comment.setPost(post);
            comment.setUser(writer);
            commentRepository.save(comment);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
