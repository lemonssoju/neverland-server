package com.lesso.neverland.group.domain;

import com.lesso.neverland.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Group extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupIdx;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false)
    private String subName;

    @Column(nullable = false)
    private String groupImage;

    // member
}
