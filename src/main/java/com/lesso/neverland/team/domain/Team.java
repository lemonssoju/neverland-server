package com.lesso.neverland.team.domain;

import com.lesso.neverland.common.BaseEntity;
import com.lesso.neverland.user.domain.User;
import com.lesso.neverland.user.domain.UserTeam;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamIdx;

    @ManyToOne
    @JoinColumn(nullable = false, name = "admin")
    private User admin; // 단방향 매핑

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false)
    private String subName;

    @Column(nullable = false)
    private String teamImage;

    @OneToMany(mappedBy = "team")
    private List<UserTeam> userTeams = new ArrayList<>();
}
