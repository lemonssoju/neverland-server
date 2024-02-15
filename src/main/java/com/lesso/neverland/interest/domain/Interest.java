package com.lesso.neverland.interest.domain;

import com.lesso.neverland.common.BaseEntity;
import com.lesso.neverland.common.enums.Contents;
import com.lesso.neverland.user.domain.User;
import jakarta.persistence.*;

@Entity
public class Interest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interestIdx;

    @Column(nullable = false)
    private Contents contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user")
    private User user;

    public void setUser(User user) {
        this.user = user;
        user.getInterests().add(this);
    }
}
