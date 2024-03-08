package com.lesso.neverland.profile.repository;

import com.lesso.neverland.profile.domain.Thumbnail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThumbnailRepository extends JpaRepository<Thumbnail, Long> {
}
