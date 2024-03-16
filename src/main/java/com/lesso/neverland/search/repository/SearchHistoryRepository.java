package com.lesso.neverland.search.repository;

import com.lesso.neverland.search.domain.SearchHistory;
import com.lesso.neverland.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    List<SearchHistory> findByUserOrderByCreatedDateDesc(User user);
}
