package com.lesso.neverland.album.presentation;

import com.lesso.neverland.album.application.AlbumService;
import com.lesso.neverland.album.dto.AlbumImageRequest;
import com.lesso.neverland.common.base.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.lesso.neverland.common.constants.RequestURI.album;

@RestController
@RequiredArgsConstructor
@RequestMapping(album)
public class AlbumController {

    private final AlbumService albumService;

    // 추억 이미지 등록
    @PostMapping("/{albumIdx}/image")
    public BaseResponse<String> uploadAlbumImage(@PathVariable("groupIdx") Long groupIdx, @PathVariable("albumIdx") Long albumIdx, @RequestBody AlbumImageRequest albumImageRequest) {
        return albumService.uploadAlbumImage(groupIdx, albumIdx, albumImageRequest);
    }
}
