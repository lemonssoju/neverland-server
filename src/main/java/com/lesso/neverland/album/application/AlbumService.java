package com.lesso.neverland.album.application;

import com.lesso.neverland.album.domain.Album;
import com.lesso.neverland.album.dto.*;
import com.lesso.neverland.album.repository.AlbumRepository;
import com.lesso.neverland.comment.dto.CommentDto;
import com.lesso.neverland.common.base.BaseException;
import com.lesso.neverland.common.base.BaseResponse;
import com.lesso.neverland.group.domain.Team;
import com.lesso.neverland.group.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

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

    // 앨범 상세 조회
    public BaseResponse<AlbumDetailResponse> getAlbumDetail(Long groupIdx, Long albumIdx) {
        Team group = groupRepository.findById(groupIdx).orElseThrow(() -> new BaseException(INVALID_GROUP_IDX));
        Album album = albumRepository.findById(albumIdx).orElseThrow(() -> new BaseException(INVALID_ALBUM_IDX));
        if (!album.getPuzzle().getTeam().equals(group)) throw new BaseException(NO_GROUP_ALBUM);

        List<String> memberList = album.getPuzzle().getPuzzleMembers().stream()
                .map(puzzleMember -> puzzleMember.getUser().getProfile().getNickname()).toList();

        List<CommentDto> commentList = album.getComments().stream()
                .filter(comment -> "active".equals(comment.getStatus()))
                .map(comment -> new CommentDto(
                        comment.getCommentIdx(),
                        comment.getUser().getProfile().getNickname(),
                        comment.getUser().getProfile().getProfileImage(),
                        comment.getCreatedDate().toString(),
                        comment.getContent())).toList();

        AlbumDetailResponse albumDetailResponse = new AlbumDetailResponse(album.getPuzzle().getTitle(), album.getPuzzle().getPuzzleDate().toString(),
                album.getPuzzle().getLocation().getLocation(), memberList, album.getAlbumImage(), album.getContent(), album.getPuzzle().getPuzzleIdx(), commentList);
        return new BaseResponse<>(albumDetailResponse);
    }

    // 앨범 목록 조회(sortType="time", "location")
    public BaseResponse<?> getAlbumList(Long groupIdx, String sortType) {
        Team group = groupRepository.findById(groupIdx).orElseThrow(() -> new BaseException(INVALID_GROUP_IDX));
        List<Album> albumList = albumRepository.findByTeamOrderByCreatedDateDesc(group);

        if (sortType.equals("time")) {
            List<AlbumByTimeDto> albumDtoList = albumList.stream().map(AlbumByTimeDto::from).toList();
            return new BaseResponse<>(new AlbumListByTimeResponse(albumDtoList));
        } else {
            List<AlbumByLocationDto> albumDtoList = albumList.stream().map(AlbumByLocationDto::from).toList();
            return new BaseResponse<>(new AlbumListByLocationResponse(albumDtoList));
        }
    }
}
