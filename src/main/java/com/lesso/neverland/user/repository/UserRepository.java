package com.lesso.neverland.user.repository;

import com.lesso.neverland.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
