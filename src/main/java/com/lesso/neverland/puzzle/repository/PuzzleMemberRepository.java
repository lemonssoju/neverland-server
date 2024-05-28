package com.lesso.neverland.puzzle.repository;

import com.lesso.neverland.puzzle.domain.PuzzleMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PuzzleMemberRepository extends JpaRepository<PuzzleMember, Long> {
}
