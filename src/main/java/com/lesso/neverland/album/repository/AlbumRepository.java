package com.lesso.neverland.album.repository;

import com.lesso.neverland.album.domain.Album;
import com.lesso.neverland.group.domain.Team;
import com.lesso.neverland.puzzle.domain.Puzzle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findByTeamOrderByCreatedDateDesc(Team team);
    boolean existsByPuzzle(Puzzle puzzle);
}
