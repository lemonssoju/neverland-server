package com.lesso.neverland.user.repository;

import com.lesso.neverland.group.domain.Team;
import com.lesso.neverland.user.domain.User;
import com.lesso.neverland.user.domain.UserTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {
    UserTeam findByUserAndTeam(User user, Team team);
    boolean existsByUserAndTeam(User user, Team team);
}
