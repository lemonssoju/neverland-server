package com.lesso.neverland.guestMemo.application;

import com.lesso.neverland.common.base.BaseException;
import com.lesso.neverland.common.base.BaseResponse;
import com.lesso.neverland.guestMemo.domain.GuestMemo;
import com.lesso.neverland.guestMemo.dto.GetGuestMemoListRequest;
import com.lesso.neverland.guestMemo.dto.GuestMemoDto;
import com.lesso.neverland.guestMemo.dto.GuestMemoListResponse;
import com.lesso.neverland.guestMemo.dto.PostGuestMemoRequest;
import com.lesso.neverland.guestMemo.repository.GuestMemoRepository;
import com.lesso.neverland.user.application.UserService;
import com.lesso.neverland.user.domain.User;
import com.lesso.neverland.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.lesso.neverland.common.base.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class GuestMemoService {

    UserRepository userRepository;
    UserService userService;
    GuestMemoRepository guestMemoRepository;

    // 방명록 등록
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<String> postGuestMemo(PostGuestMemoRequest postGuestMemoRequest) {
        User writer = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        User profileOwner = userRepository.findById(postGuestMemoRequest.userIdx()).orElseThrow(() -> new BaseException(NO_MATCH_USER));
        if (postGuestMemoRequest.content().length() > 50) throw new BaseException(TOO_LONG_CONTENT);

        GuestMemo memo = new GuestMemo(profileOwner, writer, postGuestMemoRequest.content());
        guestMemoRepository.save(memo);
        memo.setUser(profileOwner);
        return new BaseResponse<>(SUCCESS);
    }

    // 방명록 목록 조회
    public BaseResponse<GuestMemoListResponse> getGuestMemoList(GetGuestMemoListRequest getGuestMemoListRequest) {
        User writer = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        User profileOwner = userRepository.findById(getGuestMemoListRequest.profileOwnerIdx()).orElseThrow(() -> new BaseException(NO_MATCH_USER));

        List<GuestMemo> memoList = profileOwner.getMemos();
        List<GuestMemoDto> memoDtoList = memoList.stream()
                .map(memo -> new GuestMemoDto(
                        memo.getWriter().getProfile().getNickname(),
                        memo.getWriter().getProfile().getProfileImage(),
                        memo.getContent(),
                        memo.getCreatedDate())).collect(Collectors.toList());
        return new BaseResponse<>(new GuestMemoListResponse(memoDtoList));
    }
}
