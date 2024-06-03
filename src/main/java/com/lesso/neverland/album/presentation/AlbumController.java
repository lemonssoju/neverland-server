package com.lesso.neverland.album.presentation;

import com.lesso.neverland.album.application.AlbumService;
import com.lesso.neverland.album.dto.AlbumDetailResponse;
import com.lesso.neverland.common.base.BaseException;
import com.lesso.neverland.common.base.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.lesso.neverland.common.base.BaseResponseStatus.IMAGE_UPLOAD_FAIL;
import static com.lesso.neverland.common.constants.RequestURI.album;

@RestController
@RequiredArgsConstructor
@RequestMapping(album)
public class AlbumController {

    private final AlbumService albumService;

    // 추억 이미지 등록
    @PostMapping("/{albumIdx}/image")
    public BaseResponse<String> uploadAlbumImage(@PathVariable("groupIdx") Long groupIdx, @PathVariable("albumIdx") Long albumIdx, @RequestPart MultipartFile image) {
        try {
            return albumService.uploadAlbumImage(groupIdx, albumIdx, image);
        } catch (IOException e) {
            throw new BaseException(IMAGE_UPLOAD_FAIL);
        }
    }

    // 앨범 상세 조회
    @GetMapping("/{albumIdx}")
    public BaseResponse<AlbumDetailResponse> getAlbumDetail(@PathVariable("groupIdx") Long groupIdx, @PathVariable("albumIdx") Long albumIdx) {
        return albumService.getAlbumDetail(groupIdx, albumIdx);
    }

    // 앨범 목록 조회
    @GetMapping("")
    public BaseResponse<?> getAlbumList(@PathVariable("groupIdx") Long groupIdx, @RequestParam String sortType) {
        return albumService.getAlbumList(groupIdx, sortType);
    }
}
