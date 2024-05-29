package com.lesso.neverland.album.application;

import com.lesso.neverland.album.domain.Album;
import com.lesso.neverland.album.dto.AlbumImageRequest;
import com.lesso.neverland.album.repository.AlbumRepository;
import com.lesso.neverland.common.base.BaseException;
import com.lesso.neverland.common.base.BaseResponse;
import com.lesso.neverland.group.domain.Team;
import com.lesso.neverland.group.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.lesso.neverland.common.base.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final GroupRepository groupRepository;
    private final AlbumRepository albumRepository;

    // 추억 이미지 등록
    public BaseResponse<String> uploadAlbumImage(Long groupIdx, Long albumIdx, AlbumImageRequest albumImageRequest) {
        Team group = groupRepository.findById(groupIdx).orElseThrow(() -> new BaseException(INVALID_GROUP_IDX));
        Album album = albumRepository.findById(albumIdx).orElseThrow(() -> new BaseException(INVALID_ALBUM_IDX));
        if (!album.getPuzzle().getTeam().equals(group)) throw new BaseException(NO_GROUP_ALBUM);

        album.saveAlbumImage(albumImageRequest.albumImage());
        albumRepository.save(album);

        return new BaseResponse<>(SUCCESS);
    }
}
