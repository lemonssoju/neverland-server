package com.lesso.neverland.group.domain;

import com.lesso.neverland.common.BaseEntity;
import com.lesso.neverland.user.domain.User;
import com.lesso.neverland.user.domain.UserTeam;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

import static com.lesso.neverland.common.constants.Constants.INACTIVE;

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
    @Where(clause = "status = 'ACTIVE'")
    private List<UserTeam> userTeams = new ArrayList<>();

    @Builder
    public Team(User admin, String name, String subName, String teamImage) {
        this.admin = admin;
        this.name = name;
        this.subName = subName;
        this.teamImage = teamImage;
    }

    public void modifyName(String name) { this.name = name; }
    public void modifySubName(String subName) { this.subName = subName; }
    public void modifyImage(String imageUrl) {this.teamImage = imageUrl; }
    public void delete() { this.setStatus(INACTIVE);}
}
