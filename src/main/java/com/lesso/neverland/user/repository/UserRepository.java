package com.lesso.neverland.user.repository;

import com.lesso.neverland.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(String loginId);
    Optional<User> findByUserIdxAndStatusEquals(Long userIdx, String status);
}
