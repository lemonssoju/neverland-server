package com.lesso.neverland.follow.repository;

import com.lesso.neverland.follow.domain.Follow;
import com.lesso.neverland.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Integer countByFollowingUser(User user);
    Integer countByFollowedUser(User user);
    List<Follow> findByFollowingUserAndStatusEquals(User user, String status);
    List<Follow> findByFollowedUserAndStatusEquals(User user, String status);
}
