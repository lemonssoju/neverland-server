package com.lesso.neverland.user.domain;

import com.lesso.neverland.common.BaseEntity;
import com.lesso.neverland.common.enums.ThumbnailOrder;
import jakarta.persistence.*;

@Entity
public class Thumbnail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long thumbnailIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ThumbnailOrder thumbnailOrder;

    @Column(nullable = false)
    private String imageUrl;

    public void setUser(User user) {
        this.user = user;
        user.getThumbnails().add(this);
    }
}
