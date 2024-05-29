package com.lesso.neverland.puzzle.repository;

import com.lesso.neverland.puzzle.domain.PuzzlePiece;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PuzzlePieceRepository extends JpaRepository<PuzzlePiece, Long> {
}
