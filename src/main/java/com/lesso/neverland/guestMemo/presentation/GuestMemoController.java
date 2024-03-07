package com.lesso.neverland.guestMemo.presentation;

import com.lesso.neverland.common.BaseException;
import com.lesso.neverland.common.BaseResponse;
import com.lesso.neverland.guestMemo.application.GuestMemoService;
import com.lesso.neverland.guestMemo.dto.GetGuestMemoListRequest;
import com.lesso.neverland.guestMemo.dto.GuestMemoListResponse;
import com.lesso.neverland.guestMemo.dto.PostGuestMemoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.lesso.neverland.common.BaseResponseStatus.SUCCESS;
import static com.lesso.neverland.common.constants.RequestURI.memo;

@RestController
@RequiredArgsConstructor
@RequestMapping(memo)
public class GuestMemoController {

    private final GuestMemoService guestMemoService;

    // 방명록 등록
    @PostMapping("")
    public BaseResponse<String> postGuestMemo(@RequestBody PostGuestMemoRequest postGuestMemoRequest) {
        try {
            guestMemoService.postGuestMemo(postGuestMemoRequest);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 방명록 목록 조회
    @GetMapping("")
    public BaseResponse<GuestMemoListResponse> getGuestMemoList(@RequestBody GetGuestMemoListRequest getGuestMemoListRequest) {
        try {
            return new BaseResponse<>(guestMemoService.getGuestMemoList(getGuestMemoListRequest));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
