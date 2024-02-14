package com.lesso.neverland.user.domain.entity;

import com.lesso.neverland.common.BaseEntity;
import com.lesso.neverland.common.enums.Contents;
import jakarta.persistence.*;

@Entity
public class Interest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interestIdx;

    @Column(nullable = false)
    private Contents contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "userIdx")
    private User user;

    public void setUser(User user) {
        this.user = user;
        user.getInterests().add(this);
    }
}
