package com.lesso.neverland.group.repository;

import com.lesso.neverland.group.domain.Team;
import com.lesso.neverland.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Team, Long> {
    List<Team> findByAdminAndStatusEquals(User admin, String status);
    boolean existsByJoinCode(Integer joinCode);
    Optional<Team> findByJoinCode(Integer joinCode);

}
