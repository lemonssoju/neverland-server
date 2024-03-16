package com.lesso.neverland.search.application;

import com.lesso.neverland.common.BaseException;
import com.lesso.neverland.common.enums.Contents;
import com.lesso.neverland.post.domain.Post;
import com.lesso.neverland.post.domain.PostTag;
import com.lesso.neverland.post.repository.PostRepository;
import com.lesso.neverland.post.repository.PostTagRepository;
import com.lesso.neverland.search.domain.SearchHistory;
import com.lesso.neverland.search.dto.PostSearchDto;
import com.lesso.neverland.search.dto.PostSearchResponse;
import com.lesso.neverland.search.dto.UserSearchDto;
import com.lesso.neverland.search.dto.UserSearchResponse;
import com.lesso.neverland.search.repository.SearchHistoryRepository;
import com.lesso.neverland.user.application.UserService;
import com.lesso.neverland.user.domain.User;
import com.lesso.neverland.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.lesso.neverland.common.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class SearchService {

    UserRepository userRepository;
    PostRepository postRepository;
    PostTagRepository postTagRepository;
    SearchHistoryRepository searchHistoryRepository;
    UserService userService;

    // user 검색
    public UserSearchResponse searchUser(String nickname) throws BaseException {
        try {
            User currentUser = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));

            List<User> userList = userRepository.searchUserByNickname(nickname);
            if (userList == null || userList.isEmpty()) {
                saveSearchHistory(currentUser, nickname);
                return new UserSearchResponse(Collections.emptyList());
            } else {
                List<UserSearchDto> searchResultList = userList.stream()
                        .map(user -> new UserSearchDto(user.getUserIdx(), user.getProfile().getNickname(), user.getProfile().getProfileImage())).toList();
                saveSearchHistory(currentUser, nickname);
                return new UserSearchResponse(searchResultList);
            }
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // tag 검색
    public PostSearchResponse searchTag(String tag) throws BaseException {
        try {
            Contents tagName = Contents.getEnumByName(tag);
            List<PostTag> postTags = postTagRepository.findByTagName(tagName);

            List<Post> tagSearchList = postTags.stream().map(PostTag::getPost).distinct().toList(); // 중복 post 제거
            if (tagSearchList.isEmpty()) {
                return new PostSearchResponse(Collections.emptyList());
            } else {
                List<PostSearchDto> searchResultList = getPostSearchDtoList(tagSearchList);
                return new PostSearchResponse(searchResultList);
            }
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private List<PostSearchDto> getPostSearchDtoList(List<Post> tagSearchList) {
        return tagSearchList.stream()
                .map(post -> new PostSearchDto(post.getPostIdx(), post.getContent(), post.getTitle(), post.getPostImage(),
                        post.getPostTags().stream().map(postTag -> postTag.getTagName().getName()).toList())).toList();
    }

    // post(title, content) 검색
    public PostSearchResponse searchPost(String keyword) throws BaseException {
        try {
            List<Post> titleAndContentSearchList = postRepository.searchTitleAndContentByKeyword(keyword);
            if (titleAndContentSearchList == null || titleAndContentSearchList.isEmpty()) {
                return new PostSearchResponse(Collections.emptyList());
            } else {
                List<PostSearchDto> searchResultList = getPostSearchDtoList(titleAndContentSearchList);
                return new PostSearchResponse(searchResultList);
            }
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private void saveSearchHistory(User user, String searchWord) {
        SearchHistory searchHistory = new SearchHistory(user, searchWord);
        searchHistory.setUser(user);
        searchHistoryRepository.save(searchHistory);
    }
}
