package com.lesso.neverland.comment.domain;

import com.lesso.neverland.album.domain.Album;
import com.lesso.neverland.common.base.BaseEntity;
import com.lesso.neverland.user.domain.User;
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
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "puzzle")
    private Album album;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    private User user;

    @Column(nullable = false)
    private String content;

    @Builder
    public Comment(Album album, User user, String content) {
        this.album = album;
        this.user = user;
        this.content = content;
    }

    public void setAlbum(Album album) {
        this.album = album;
        album.getComments().add(this);
    }

    public void setUser(User user) {
        this.user = user;
        user.getComments().add(this);
    }

    public void delete() {
        this.setStatus(INACTIVE);
    }
    public void modifyContent(String content) { this.content = content; }
}
