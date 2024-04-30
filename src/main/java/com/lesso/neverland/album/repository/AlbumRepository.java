package com.lesso.neverland.album.repository;

import com.lesso.neverland.album.domain.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Long> {
}
