package com.lesso.neverland.post.domain;

import com.lesso.neverland.common.base.BaseEntity;
import com.lesso.neverland.common.enums.Contents;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import static com.lesso.neverland.common.constants.Constants.INACTIVE;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostTag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postTagIdx;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Contents tagName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post")
    private Post post;

    public void setPost(Post post) {
        this.post = post;
        post.getPostTags().add(this);
    }

    public void delete() {
        this.setStatus(INACTIVE);
    }
}
