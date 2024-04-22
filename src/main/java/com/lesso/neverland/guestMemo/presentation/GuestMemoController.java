package com.lesso.neverland.guestMemo.presentation;

import com.lesso.neverland.common.base.BaseResponse;
import com.lesso.neverland.guestMemo.application.GuestMemoService;
import com.lesso.neverland.guestMemo.dto.GetGuestMemoListRequest;
import com.lesso.neverland.guestMemo.dto.GuestMemoListResponse;
import com.lesso.neverland.guestMemo.dto.PostGuestMemoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.lesso.neverland.common.constants.RequestURI.memo;

@RestController
@RequiredArgsConstructor
@RequestMapping(memo)
public class GuestMemoController {

    private final GuestMemoService guestMemoService;

    // 방명록 등록
    @PostMapping("")
    public BaseResponse<String> postGuestMemo(@RequestBody PostGuestMemoRequest postGuestMemoRequest) {
        return guestMemoService.postGuestMemo(postGuestMemoRequest);
    }

    // 방명록 목록 조회
    @GetMapping("")
    public BaseResponse<GuestMemoListResponse> getGuestMemoList(@RequestBody GetGuestMemoListRequest getGuestMemoListRequest) {
        return guestMemoService.getGuestMemoList(getGuestMemoListRequest);
    }
}
