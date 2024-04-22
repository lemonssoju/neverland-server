package com.lesso.neverland.search.domain;

import com.lesso.neverland.common.base.BaseEntity;
import com.lesso.neverland.user.domain.User;
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
public class SearchHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long searchHistoryIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    private User user;

    @Column(nullable = false)
    private String searchWord;

    public void setUser(User user) {
        this.user = user;
        user.getSearchHistories().add(this);
    }

    public SearchHistory(User user, String searchWord) {
        this.user = user;
        this.searchWord = searchWord;
    }

    public void delete() {
        this.setStatus(INACTIVE);
    }
}
