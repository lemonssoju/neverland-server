package com.lesso.neverland.post.repository;

import com.lesso.neverland.common.enums.Contents;
import com.lesso.neverland.group.domain.Team;
import com.lesso.neverland.post.domain.Post;
import com.lesso.neverland.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findTop3ByPostTags_TagNameIn(List<Contents> interests);
    List<Post> findByTeamAndStatusEqualsOrderByCreatedDateDesc(Team group, String status);
    List<Post> findByTeamAndStatusEquals(Team group, String status);
    List<Post> findByUserAndStatusEquals(User user,String status);
    @Query("SELECT p FROM Post p WHERE p.title LIKE CONCAT('%', :keyword, '%') OR p.content LIKE CONCAT('%', :keyword, '%')")
    List<Post> searchTitleAndContentByKeyword(@Param("keyword") String keyword);
}
