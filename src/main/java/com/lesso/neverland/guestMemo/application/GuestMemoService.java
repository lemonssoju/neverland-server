package com.lesso.neverland.guestMemo.application;

import com.lesso.neverland.common.BaseException;
import com.lesso.neverland.guestMemo.domain.GuestMemo;
import com.lesso.neverland.guestMemo.dto.PostGuestMemoRequest;
import com.lesso.neverland.guestMemo.repository.GuestMemoRepository;
import com.lesso.neverland.user.application.UserService;
import com.lesso.neverland.user.domain.User;
import com.lesso.neverland.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.lesso.neverland.common.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class GuestMemoService {

    UserRepository userRepository;
    UserService userService;
    GuestMemoRepository guestMemoRepository;

    // 방명록 등록
    public void postGuestMemo(PostGuestMemoRequest postGuestMemoRequest) throws BaseException {
        try {
            User writer = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            User profileOwner = userRepository.findById(postGuestMemoRequest.userIdx()).orElseThrow(() -> new BaseException(NO_MATCH_USER));
            if (postGuestMemoRequest.content().length() > 50) throw new BaseException(TOO_LONG_CONTENT);

            GuestMemo memo = new GuestMemo(profileOwner, writer, postGuestMemoRequest.content());
            guestMemoRepository.save(memo);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
