package com.lesso.neverland.follow.repository;

import com.lesso.neverland.follow.domain.Follow;
import com.lesso.neverland.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Integer countByFollowingUser(User user);
    Integer countByFollowedUser(User user);
    List<Follow> findByFollowingUserAndStatusEquals(User user, String status);
    List<Follow> findByFollowedUserAndStatusEquals(User user, String status);
    Optional<Follow> findByFollowingUserAndFollowedUserAndStatusEquals(User user, User writer, String status);
    @Query("SELECT f FROM Follow f WHERE f.followingUser = :followingUser AND f.followedUser.profile.nickname LIKE CONCAT('%', :nickname, '%') AND f.status = 'ACTIVE'")
    List<Follow> findByFollowingUserAndFollowedNickname(@Param("followingUser") User followingUser, @Param("nickname") String nickname);

    @Query("SELECT f FROM Follow f WHERE f.followedUser = :followeduser AND f.followingUser.profile.nickname LIKE CONCAT('%', :nickname, '%') AND f.status = 'ACTIVE'")
    List<Follow> findByFollowedUserAndFollowingNickname(@Param("followedUser") User followedUser, @Param("nickname") String nickname);
}
