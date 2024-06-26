package com.lesso.neverland.puzzle.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lesso.neverland.album.domain.Album;
import com.lesso.neverland.album.repository.AlbumRepository;
import com.lesso.neverland.common.base.BaseException;
import com.lesso.neverland.common.base.BaseResponse;
import com.lesso.neverland.common.image.ImageService;
import com.lesso.neverland.gpt.application.GptService;
import com.lesso.neverland.gpt.dto.GptResponseDto;
import com.lesso.neverland.puzzle.domain.PuzzleLocation;
import com.lesso.neverland.puzzle.dto.CompletePuzzleResponse;
import com.lesso.neverland.gpt.dto.GptResponse;
import com.lesso.neverland.group.domain.Team;
import com.lesso.neverland.group.dto.GroupPuzzleDto;
import com.lesso.neverland.group.dto.GroupPuzzleListResponse;
import com.lesso.neverland.group.repository.GroupRepository;
import com.lesso.neverland.puzzle.domain.Puzzle;
import com.lesso.neverland.puzzle.domain.PuzzleMember;
import com.lesso.neverland.puzzle.domain.PuzzlePiece;
import com.lesso.neverland.puzzle.dto.*;
import com.lesso.neverland.puzzle.repository.PuzzleMemberRepository;
import com.lesso.neverland.puzzle.repository.PuzzlePieceRepository;
import com.lesso.neverland.puzzle.repository.PuzzleRepository;
import com.lesso.neverland.user.application.UserService;
import com.lesso.neverland.user.domain.User;
import com.lesso.neverland.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.lesso.neverland.common.base.BaseResponseStatus.*;
import static com.lesso.neverland.common.constants.Constants.ACTIVE;
import static com.lesso.neverland.common.constants.Constants.INACTIVE;

@Service
@RequiredArgsConstructor
public class PuzzleService {

    private final String API_URL = "https://dapi.kakao.com/v2/local/search/address.json?query=";
    @Value("${kakao.maps.api-key}")
    private String API_KEY;

    private final UserService userService;
    private final PuzzleRepository puzzleRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final ImageService imageService;
    private final PuzzleMemberRepository puzzleMemberRepository;
    private final PuzzlePieceRepository puzzlePieceRepository;
    private final GptService gptService;
    private final AlbumRepository albumRepository;

    // 퍼즐 목록 조회
    public BaseResponse<GroupPuzzleListResponse> getGroupPuzzleList(Long groupIdx) {
        User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        Team group = groupRepository.findById(groupIdx).orElseThrow(() -> new BaseException(INVALID_GROUP_IDX));

        List<Puzzle> groupPuzzleList = puzzleRepository.findByTeamAndStatusEqualsOrderByCreatedDateDesc(group, ACTIVE);
        List<GroupPuzzleDto> groupPuzzleListDto = getGroupPuzzleDtos(groupPuzzleList);

        return new BaseResponse<>(new GroupPuzzleListResponse(groupPuzzleListDto));
    }

    @NotNull
    private static List<GroupPuzzleDto> getGroupPuzzleDtos(List<Puzzle> groupPuzzleList) {
        return groupPuzzleList.stream()
                .map(puzzle -> new GroupPuzzleDto(
                        puzzle.getPuzzleIdx(),
                        puzzle.getTitle(),
                        puzzle.getPuzzleImage(),
                        puzzle.getUser().getProfile().getNickname(),
                        puzzle.getCreatedDate().toString(),
                        puzzle.getLocation().getLocation())).toList();
    }

    // 퍼즐 상세 조회
    public BaseResponse<PuzzleDetailResponse> getPuzzleDetail(Long groupIdx, Long puzzleIdx) {
        Team group = groupRepository.findById(groupIdx).orElseThrow(() -> new BaseException(INVALID_GROUP_IDX));
        User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        Puzzle puzzle = puzzleRepository.findById(puzzleIdx).orElseThrow(() -> new BaseException(INVALID_PUZZLE_IDX));

        if (!puzzle.getTeam().equals(group)) throw new BaseException(NO_GROUP_PUZZLE);
        if (puzzle.getStatus().equals(INACTIVE)) throw new BaseException(ALREADY_DELETED_PUZZLE);

        boolean isWriter = puzzle.getUser().equals(user);
        boolean hasWrite = puzzlePieceRepository.existsByPuzzleAndUser(puzzle, user);
        boolean hasAlbum = albumRepository.existsByPuzzle(puzzle);

        PuzzleDetailResponse puzzleDetail = new PuzzleDetailResponse(puzzle.getLocation().getLocation(), puzzle.getPuzzleImage(),
                puzzle.getPuzzleDate().toString(), puzzle.getCreatedDate().toString(), puzzle.getUser().getProfile().getNickname(), puzzle.getTitle(), puzzle.getContent(),
                getMemberImageList(puzzle), getMemberNicknameList(puzzle), puzzle.getPuzzleMembers().size(), puzzle.getPuzzlePieces().size()+1, isWriter, hasWrite, hasAlbum,
                getPuzzlePieceList(puzzle));
        return new BaseResponse<>(puzzleDetail);
    }

    // 퍼즐피스 목록 조회
    private List<PuzzlePieceDto> getPuzzlePieceList(Puzzle puzzle) {
        return puzzle.getPuzzlePieces().stream()
                .map(puzzlePiece -> new PuzzlePieceDto(
                        puzzlePiece.getUser().getProfile().getNickname(),
                        puzzlePiece.getUser().getProfile().getProfileImage(),
                        puzzlePiece.getContent()
                )).toList();
    }

    // puzzleMember 3명의 프로필 이미지 조회
    private List<String> getMemberImageList(Puzzle puzzle) {
        List<String> imageList = new ArrayList<>();
        imageList.add(puzzle.getUser().getProfile().getProfileImage());

        List<String> memberImages = puzzle.getPuzzleMembers().stream()
                .map(puzzleMember -> puzzleMember.getUser().getProfile().getProfileImage())
                .limit(2).toList();
        imageList.addAll(memberImages);
        return imageList;
    }

    private List<String> getMemberNicknameList(Puzzle puzzle) {
        return puzzle.getPuzzleMembers().stream()
                .map(puzzleMember -> puzzleMember.getUser().getProfile().getNickname()).toList();
    }

    // 퍼즐 생성
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<CreatePuzzleResponse> createPuzzle(Long groupIdx, MultipartFile image, CreatePuzzleRequest createPuzzleRequest) throws IOException {
        Team group = groupRepository.findById(groupIdx).orElseThrow(() -> new BaseException(INVALID_GROUP_IDX));
        User writer = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));

        LocalDate puzzleDate = convertToLocalDate(createPuzzleRequest.puzzleDate());
        Puzzle newPuzzle = createPuzzle(createPuzzleRequest, group, writer, puzzleDate);
        if (image != null && !image.isEmpty()) {
            String imagePath = imageService.uploadImage("puzzle", image);
            newPuzzle.addPuzzleImage(imagePath);
        }
        addPuzzleMember(createPuzzleRequest, newPuzzle);

        return new BaseResponse<>(new CreatePuzzleResponse(newPuzzle.getPuzzleIdx()));
    }

    // Puzzle 생성 시 PuzzleMember entity 함께 생성
    private void addPuzzleMember(CreatePuzzleRequest createPuzzleRequest, Puzzle newPuzzle) {
        for (Long userIdx : createPuzzleRequest.puzzlerList()) {
            User member = userRepository.findById(userIdx).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            PuzzleMember puzzleMember = PuzzleMember.builder()
                    .user(member)
                    .puzzle(newPuzzle).build();
            puzzleMember.setUser(member);
            puzzleMember.setPuzzle(newPuzzle);

            puzzleMemberRepository.save(puzzleMember);
        }
    }

    // String -> LocalDate
    private static LocalDate convertToLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        formatter = formatter.withLocale(Locale.KOREA);

        return LocalDate.parse(date, formatter);
    }

    // puzzle entity 생성
    private Puzzle createPuzzle(CreatePuzzleRequest createPuzzleRequest, Team group, User writer, LocalDate puzzleDate) throws JsonProcessingException {
        Puzzle puzzle = Puzzle.builder()
                .user(writer)
                .team(group)
                .title(createPuzzleRequest.title())
                .content(createPuzzleRequest.content())
                .puzzleDate(puzzleDate)
                .location(convertAddressToCoordinates(createPuzzleRequest.location())).build();
        puzzleRepository.save(puzzle);
        return puzzle;
    }

    // String address값을 KakaoMap API를 활용해 x,y 좌표로 변환
    private PuzzleLocation convertAddressToCoordinates(String address) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + API_KEY);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = API_URL + address;
        String responseBody = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        JsonNode firstDocument = jsonNode.path("documents").get(0);
        String x = firstDocument.path("x").asText();
        String y = firstDocument.path("y").asText();

        return new PuzzleLocation(address, x, y);
    }

    // [작성자] 퍼즐 수정
    public BaseResponse<String> editPuzzle(Long groupIdx, Long puzzleIdx, MultipartFile newImage, EditPuzzleRequest editPuzzleRequest) throws IOException {
        User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        Puzzle puzzle = puzzleRepository.findById(puzzleIdx).orElseThrow(() -> new BaseException(INVALID_PUZZLE_IDX));
        validateWriter(user, puzzle);
        if (albumRepository.existsByPuzzle(puzzle)) throw new BaseException(ALREADY_HAS_ALBUM);

        if (editPuzzleRequest.content() != null) {
            if (!editPuzzleRequest.content().equals("") && !editPuzzleRequest.content().equals(" "))
                puzzle.editContent(editPuzzleRequest.content());
            else throw new BaseException(BLANK_PUZZLE_CONTENT);
        }
        if (newImage != null && !newImage.isEmpty()) {
            imageService.deleteImage(puzzle.getPuzzleImage());

            String newImagePath = imageService.uploadImage("puzzle", newImage);
            puzzle.modifyImage(newImagePath);
        }
        puzzleRepository.save(puzzle);
        return new BaseResponse<>(SUCCESS);
    }

    // [작성자] 퍼즐 삭제
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<String> deletePuzzle(Long groupIdx, Long puzzleIdx) {
        User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        Puzzle puzzle = puzzleRepository.findById(puzzleIdx).orElseThrow(() -> new BaseException(INVALID_PUZZLE_IDX));
        validateWriter(user, puzzle);

        puzzle.getPuzzlePieces().forEach(PuzzlePiece::delete);
        puzzle.delete();

        puzzleRepository.save(puzzle);
        return new BaseResponse<>(SUCCESS);
    }

    // MyPuzzleDto로 가공
    private List<MyPuzzleDto> convertToMyPuzzleDtoList(List<Puzzle> puzzleList) {
        return puzzleList.stream()
                .map(puzzle -> new MyPuzzleDto(
                        puzzle.getPuzzleImage(),
                        puzzle.getTitle(),
                        puzzle.getCreatedDate())).toList();
    }

    // 작성자 validation
    private static void validateWriter(User user, Puzzle puzzle) {
        if (!puzzle.getUser().equals(user)) throw new BaseException(NO_PUZZLE_WRITER);
        if (puzzle.getStatus().equals(INACTIVE)) throw new BaseException(ALREADY_DELETED_PUZZLE);
    }

    // 퍼즐러 목록 조회
    public BaseResponse<PuzzlerListResponse> getPuzzlerList(Long groupIdx) {
        Team group = groupRepository.findById(groupIdx).orElseThrow(() -> new BaseException(INVALID_GROUP_IDX));

        PuzzlerListResponse puzzlerList = new PuzzlerListResponse(
                group.getUserTeams().stream()
                        .map(userTeam -> new PuzzlerDto(
                                userTeam.getUser().getUserIdx(),
                                userTeam.getUser().getProfile().getProfileImage(),
                                userTeam.getUser().getProfile().getNickname()))
                        .toList());
        return new BaseResponse<>(puzzlerList);
    }

    // 퍼즐피스 추가
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<String> addPuzzlePiece(Long groupIdx, Long puzzleIdx, PuzzlePieceRequest puzzlePieceRequest) {
        User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        Puzzle puzzle = puzzleRepository.findById(puzzleIdx).orElseThrow(() -> new BaseException(INVALID_PUZZLE_IDX));
        validatePuzzler(user, puzzle);

        if (puzzlePieceRequest.content().length() > 100) throw new BaseException(TOO_LONG_CONTENT);
        PuzzlePiece puzzlePiece = PuzzlePiece.builder()
                .puzzle(puzzle)
                .user(user)
                .content(puzzlePieceRequest.content()).build();
        puzzlePiece.setPuzzle(puzzle);
        puzzlePiece.setUser(user);
        puzzlePieceRepository.save(puzzlePiece);

        return new BaseResponse<>(SUCCESS);
    }

    // 퍼즐러 validation
    private void validatePuzzler(User user, Puzzle puzzle) {
        if (!puzzleMemberRepository.existsByUserAndPuzzle(user, puzzle)) throw new BaseException(NO_PUZZLER);
        if (puzzle.getStatus().equals(INACTIVE)) throw new BaseException(ALREADY_DELETED_PUZZLE);
    }

    // [작성자] 퍼즐 완성하기
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<CompletePuzzleResponse> completePuzzle(Long groupIdx, Long puzzleIdx) {
        User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        Puzzle puzzle = puzzleRepository.findById(puzzleIdx).orElseThrow(() -> new BaseException(INVALID_PUZZLE_IDX));
        validateWriter(user, puzzle);

        List<String> puzzleTextList = puzzle.getPuzzlePieces().stream()
                .map(PuzzlePiece::getContent).collect(Collectors.toList());
        puzzleTextList.add(puzzle.getContent());

        // GPT 요약 수행
        GptResponse response = gptService.completion(gptService.toText(puzzleTextList));
        //GptResponseDto gptResponseDto = gptService.parseResponse(response.messages().get(0).message());
        GptResponseDto gptResponseDto = gptService.parse(response.messages().get(0).message());

        // Album 생성
        Album newAlbum = Album.builder()
                .puzzle(puzzle)
                .albumImage("")
                .team(puzzle.getTeam())
                .content(gptResponseDto.description()).build();
        albumRepository.save(newAlbum);

        CompletePuzzleResponse completePuzzleResponse = new CompletePuzzleResponse(gptResponseDto.prompt(), gptResponseDto.description(), newAlbum.getAlbumIdx());
        return new BaseResponse<>(completePuzzleResponse);
    }
}
