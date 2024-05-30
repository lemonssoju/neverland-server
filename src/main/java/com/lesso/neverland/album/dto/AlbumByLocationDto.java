package com.lesso.neverland.album.dto;

import com.lesso.neverland.album.domain.Album;

public record AlbumByLocationDto(Long albumIdx,
                                 String albumImage,
                                 String x,
                                 String y) {
    public static AlbumByLocationDto from(Album album) {
        return new AlbumByLocationDto(
                album.getAlbumIdx(),
                album.getAlbumImage(),
                album.getPuzzle().getLocation().getX(),
                album.getPuzzle().getLocation().getY());
    }
}
