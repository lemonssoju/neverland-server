package com.lesso.neverland.group.repository;

import com.lesso.neverland.group.domain.Team;
import com.lesso.neverland.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Team, Long> {
    List<Team> findByAdmin(User admin);
}
