package com.lesso.neverland.user.domain;

import com.lesso.neverland.common.BaseEntity;
import com.lesso.neverland.group.domain.Group;
import jakarta.persistence.*;

@Entity
public class UserGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userGroupIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "group")
    private Group group;

    public void setUser(User user) {
        this.user = user;
        user.getUserGroups().add(this);
    }

    public void setGroup(Group group) {
        this.group = group;
        group.getUserGroups().add(this);
    }
}
