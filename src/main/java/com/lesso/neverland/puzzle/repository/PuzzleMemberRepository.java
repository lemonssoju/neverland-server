package com.lesso.neverland.puzzle.repository;

import com.lesso.neverland.puzzle.domain.Puzzle;
import com.lesso.neverland.puzzle.domain.PuzzleMember;
import com.lesso.neverland.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PuzzleMemberRepository extends JpaRepository<PuzzleMember, Long> {
    boolean existsByUserAndPuzzle(User user, Puzzle puzzle);
}
