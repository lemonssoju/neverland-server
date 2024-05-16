package com.lesso.neverland.puzzle.repository;

import com.lesso.neverland.group.domain.Team;
import com.lesso.neverland.puzzle.domain.Puzzle;
import com.lesso.neverland.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PuzzleRepository extends JpaRepository<Puzzle, Long> {
    List<Puzzle> findByTeamAndStatusEqualsOrderByCreatedDateDesc(Team group, String status);
    Puzzle findTopByTeamAndStatusEqualsOrderByCreatedDateDesc(Team group, String status);
    List<Puzzle> findByTeamAndStatusEquals(Team group, String status);
    List<Puzzle> findByUserAndStatusEquals(User user, String status);
    @Query("SELECT p FROM Puzzle p WHERE p.title LIKE CONCAT('%', :keyword, '%') OR p.content LIKE CONCAT('%', :keyword, '%')")
    List<Puzzle> searchTitleAndContentByKeyword(@Param("keyword") String keyword);
}
