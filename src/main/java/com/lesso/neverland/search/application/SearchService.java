package com.lesso.neverland.search.application;

import com.lesso.neverland.common.BaseException;
import com.lesso.neverland.common.enums.Contents;
import com.lesso.neverland.interest.domain.Interest;
import com.lesso.neverland.interest.repository.InterestRepository;
import com.lesso.neverland.post.domain.Post;
import com.lesso.neverland.post.domain.PostTag;
import com.lesso.neverland.post.repository.PostRepository;
import com.lesso.neverland.post.repository.PostTagRepository;
import com.lesso.neverland.search.domain.SearchHistory;
import com.lesso.neverland.search.dto.*;
import com.lesso.neverland.search.repository.SearchHistoryRepository;
import com.lesso.neverland.user.application.UserService;
import com.lesso.neverland.user.domain.User;
import com.lesso.neverland.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.lesso.neverland.common.BaseResponseStatus.*;
import static com.lesso.neverland.common.constants.Constants.ACTIVE;

@Service
@RequiredArgsConstructor
public class SearchService {

    UserRepository userRepository;
    PostRepository postRepository;
    PostTagRepository postTagRepository;
    SearchHistoryRepository searchHistoryRepository;
    UserService userService;
    InterestRepository interestRepository;

    // user 검색
    public UserSearchResponse searchUser(String nickname) {
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
    }

    // tag 검색
    public PostSearchResponse searchTag(String tag) {
        User currentUser = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));

        Contents tagName = Contents.getEnumByName(tag);
        List<PostTag> postTags = postTagRepository.findByTagName(tagName);

        List<Post> tagSearchList = postTags.stream().map(PostTag::getPost).distinct().toList(); // 중복 post 제거
        if (tagSearchList.isEmpty()) {
            saveSearchHistory(currentUser, tag);
            return new PostSearchResponse(Collections.emptyList());
        } else {
            List<PostSearchDto> searchResultList = getPostSearchDtoList(tagSearchList);
            saveSearchHistory(currentUser, tag);
            return new PostSearchResponse(searchResultList);
        }
    }

    // post(title, content) 검색
    public PostSearchResponse searchPost(String keyword) {
        User currentUser = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));

        List<Post> titleAndContentSearchList = postRepository.searchTitleAndContentByKeyword(keyword);
        if (titleAndContentSearchList == null || titleAndContentSearchList.isEmpty()) {
            saveSearchHistory(currentUser, keyword);
            return new PostSearchResponse(Collections.emptyList());
        } else {
            List<PostSearchDto> searchResultList = getPostSearchDtoList(titleAndContentSearchList);
            saveSearchHistory(currentUser, keyword);
            return new PostSearchResponse(searchResultList);
        }
    }

    private List<PostSearchDto> getPostSearchDtoList(List<Post> tagSearchList) {
        return tagSearchList.stream()
                .map(post -> new PostSearchDto(post.getPostIdx(), post.getContent(), post.getTitle(), post.getPostImage(),
                        post.getPostTags().stream().map(postTag -> postTag.getTagName().getContentsName()).toList())).toList();
    }

    private void saveSearchHistory(User user, String searchWord) {
        SearchHistory searchHistory = new SearchHistory(user, searchWord);
        searchHistory.setUser(user);
        searchHistoryRepository.save(searchHistory);
    }

    // 검색 화면 조회
    public SearchViewResponse getSearchView() {
        User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));

        List<Interest> userInterests = interestRepository.findByUser(user);
        List<RecommendedSearchDto> recommendedSearchList = userInterests.stream()
                .map(interest -> new RecommendedSearchDto(interest.getPreference().name())).toList();
        List<RecentSearchDto> recentSearchList = searchHistoryRepository.findByUserAndStatusEqualsOrderByCreatedDateDesc(user, ACTIVE).stream()
                .map(history -> new RecentSearchDto(history.getSearchHistoryIdx(), history.getSearchWord())).toList();
        return new SearchViewResponse(recommendedSearchList, recentSearchList);
    }

    // 최근 검색어 개별 삭제
    public void deleteRecentSearch(Long historyIdx) {
        User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        SearchHistory history = searchHistoryRepository.findById(historyIdx).orElseThrow(() -> new BaseException(INVALID_HISTORY_IDX));
        if (!history.getUser().equals(user)) throw new BaseException(NO_HISTORY_OWNER);

        history.delete();
        searchHistoryRepository.save(history);
    }

    // 최근 검색어 전체 삭제
    public void deleteAllRecentSearch() {
        User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        List<SearchHistory> historyList = searchHistoryRepository.findByUser(user);

        for (SearchHistory history : historyList) {
            history.delete();
            searchHistoryRepository.save(history);
        }
    }
}
