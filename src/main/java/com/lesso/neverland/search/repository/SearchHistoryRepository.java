package com.lesso.neverland.search.repository;

import com.lesso.neverland.search.domain.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
}
