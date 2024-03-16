package com.lesso.neverland.interest.repository;

import com.lesso.neverland.interest.domain.Interest;
import com.lesso.neverland.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {
    List<Interest> findByUser(User user);
}
