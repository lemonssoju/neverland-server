package com.lesso.neverland.user.domain;

import com.lesso.neverland.comment.domain.Comment;
import com.lesso.neverland.common.base.BaseEntity;
import com.lesso.neverland.puzzle.domain.Puzzle;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

import static com.lesso.neverland.common.constants.Constants.*;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Embedded
    private UserProfile profile;

    @OneToMany(mappedBy = "user")
    @Where(clause = "status = 'ACTIVE'")
    private List<Puzzle> puzzles = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Where(clause = "status = 'ACTIVE'")
    private List<UserTeam> userTeams = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Where(clause = "status = 'ACTIVE'")
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public User(String loginId, String password, UserProfile profile) {
        this.loginId = loginId;
        this.password = password;
        this.profile = profile;
    }

    public void login() {
        this.setStatus(ACTIVE);
    }
    public void logout() {
        this.setStatus(LOGOUT);
    }
    public void signout() {
        this.setStatus(INACTIVE);
    }
    public void modifyPassword(String newPassword) { this.password = newPassword; }
}
