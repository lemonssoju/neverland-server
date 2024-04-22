package com.lesso.neverland.profile.domain;

import com.lesso.neverland.common.base.BaseEntity;
import com.lesso.neverland.common.enums.ThumbnailOrder;
import com.lesso.neverland.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    public Thumbnail(User user, ThumbnailOrder order, String imageUrl) {
        this.user = user;
        this.thumbnailOrder = order;
        this.imageUrl = imageUrl;
    }
}
