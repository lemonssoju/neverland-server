package com.lesso.neverland.profile.repository;

import com.lesso.neverland.common.enums.ThumbnailOrder;
import com.lesso.neverland.profile.domain.Thumbnail;
import com.lesso.neverland.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ThumbnailRepository extends JpaRepository<Thumbnail, Long> {
    Optional<Thumbnail> findByUserAndThumbnailOrderAndStatusEquals(User user, ThumbnailOrder thumbnailOrder, String status);
}
