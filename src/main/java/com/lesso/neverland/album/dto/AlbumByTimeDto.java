package com.lesso.neverland.album.dto;

import com.lesso.neverland.album.domain.Album;

import java.util.ArrayList;
import java.util.List;

public record AlbumByTimeDto(Long albumIdx,
                             String title,
                             String content,
                             String albumImage,
                             String puzzleDate,
                             Integer puzzlerCount,
                             List<String> puzzlerImageList) {


    public static AlbumByTimeDto from(Album album) {
        List<String> puzzlerImageList = new ArrayList<>();
        puzzlerImageList.add(album.getPuzzle().getUser().getProfile().getProfileImage());

        puzzlerImageList.addAll(
                album.getPuzzle().getPuzzleMembers().stream()
                        .map(puzzleMember -> puzzleMember.getUser().getProfile().getProfileImage())
                        .limit(2)
                        .toList());

        return new AlbumByTimeDto(
                album.getAlbumIdx(),
                album.getPuzzle().getTitle(),
                album.getContent(),
                album.getAlbumImage(),
                album.getPuzzle().getPuzzleDate().toString(),
                album.getPuzzle().getPuzzleMembers().size(),
                puzzlerImageList
        );
    }
}
