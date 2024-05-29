package com.lesso.neverland.puzzle.repository;

import com.lesso.neverland.puzzle.domain.Puzzle;
import com.lesso.neverland.puzzle.domain.PuzzlePiece;
import com.lesso.neverland.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PuzzlePieceRepository extends JpaRepository<PuzzlePiece, Long> {
    boolean existsByPuzzleAndUser(Puzzle puzzle, User user);
}
