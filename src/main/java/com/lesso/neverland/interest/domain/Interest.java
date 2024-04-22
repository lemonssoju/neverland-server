package com.lesso.neverland.interest.domain;

import com.lesso.neverland.common.base.BaseEntity;
import com.lesso.neverland.common.enums.Contents;
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
public class Interest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interestIdx;

    @Column(nullable = false)
    private Contents contents;

    @Column(nullable = false)
    private Contents preference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user")
    private User user;

    public void setUser(User user) {
        this.user = user;
        user.getInterests().add(this);
    }

    @Builder
    public Interest(Contents contents, Contents preference, User user) {
        this.contents = contents;
        this.preference = preference;
        this.user = user;
    }
}
