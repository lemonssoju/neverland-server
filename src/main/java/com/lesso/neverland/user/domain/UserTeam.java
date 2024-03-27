package com.lesso.neverland.user.domain;

import com.lesso.neverland.common.BaseEntity;
import com.lesso.neverland.group.domain.Team;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import static com.lesso.neverland.common.constants.Constants.INACTIVE;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTeam extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userTeamIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "team")
    private Team team;

    public void setUser(User user) {
        this.user = user;
        user.getUserTeams().add(this);
    }

    public void setTeam(Team team) {
        this.team = team;
        team.getUserTeams().add(this);
    }

    @Builder
    public UserTeam(User user, Team team) {
        this.user = user;
        this.team = team;
    }

    public void delete() {
        this.setStatus(INACTIVE);
    }

    public void withdraw() {
        this.setStatus(INACTIVE);
        this.user.getUserTeams().remove(this);
        this.team.getUserTeams().remove(this);
    }
}
