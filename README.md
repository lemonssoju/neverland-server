# 🧚🏻 Neverland Server
> Server Repository for Capstone Design Project "Neverland"

## Project Structure
<details>
<summary>Code Structure</summary>
<div>
  
```
./
├── Dockerfile
├── HELP.md
├── README.md
├── build.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── neverland.iml
├── settings.gradle
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── lesso
    │   │           └── neverland
    │   │               ├── NeverlandApplication.java
    │   │               ├── album
    │   │               │   ├── application
    │   │               │   │   └── AlbumService.java
    │   │               │   ├── domain
    │   │               │   │   └── Album.java
    │   │               │   ├── dto
    │   │               │   │   ├── AlbumByLocationDto.java
    │   │               │   │   ├── AlbumByTimeDto.java
    │   │               │   │   ├── AlbumDetailResponse.java
    │   │               │   │   ├── AlbumImageRequest.java
    │   │               │   │   ├── AlbumListByLocationResponse.java
    │   │               │   │   └── AlbumListByTimeResponse.java
    │   │               │   ├── presentation
    │   │               │   │   └── AlbumController.java
    │   │               │   └── repository
    │   │               │       └── AlbumRepository.java
    │   │               ├── comment
    │   │               │   ├── application
    │   │               │   │   └── CommentService.java
    │   │               │   ├── domain
    │   │               │   │   └── Comment.java
    │   │               │   ├── dto
    │   │               │   │   ├── CommentDto.java
    │   │               │   │   ├── ModifyCommentRequest.java
    │   │               │   │   └── PostCommentRequest.java
    │   │               │   ├── presentation
    │   │               │   │   └── CommentController.java
    │   │               │   └── repository
    │   │               │       └── CommentRepository.java
    │   │               ├── common
    │   │               │   ├── YearMonthToString.java
    │   │               │   ├── base
    │   │               │   │   ├── BaseEntity.java
    │   │               │   │   ├── BaseException.java
    │   │               │   │   ├── BaseResponse.java
    │   │               │   │   ├── BaseResponseStatus.java
    │   │               │   │   └── ErrorResponse.java
    │   │               │   ├── configuration
    │   │               │   │   ├── AmazonS3Config.java
    │   │               │   │   ├── AppConfig.java
    │   │               │   │   ├── RedisConfig.java
    │   │               │   │   └── WebSecurityConfig.java
    │   │               │   ├── constants
    │   │               │   │   ├── Constants.java
    │   │               │   │   └── RequestURI.java
    │   │               │   ├── enums
    │   │               │   ├── exception
    │   │               │   │   └── GlobalExceptionHandler.java
    │   │               │   ├── image
    │   │               │   │   └── ImageService.java
    │   │               │   └── jwt
    │   │               │       ├── JwtAuthenticationFilter.java
    │   │               │       └── JwtExceptionFilter.java
    │   │               ├── gpt
    │   │               │   ├── application
    │   │               │   │   └── GptService.java
    │   │               │   ├── configuration
    │   │               │   │   └── GptConfig.java
    │   │               │   ├── domain
    │   │               │   │   └── GptProperties.java
    │   │               │   ├── dto
    │   │               │   │   ├── GptRequest.java
    │   │               │   │   ├── GptResponse.java
    │   │               │   │   └── GptResponseDto.java
    │   │               │   └── presentation
    │   │               │       └── GptController.java
    │   │               ├── group
    │   │               │   ├── application
    │   │               │   │   └── GroupService.java
    │   │               │   ├── domain
    │   │               │   │   └── Team.java
    │   │               │   ├── dto
    │   │               │   │   ├── CreateGroupRequest.java
    │   │               │   │   ├── CreateGroupResponse.java
    │   │               │   │   ├── EditGroupRequest.java
    │   │               │   │   ├── GroupEditViewResponse.java
    │   │               │   │   ├── GroupInviteResponse.java
    │   │               │   │   ├── GroupJoinResponse.java
    │   │               │   │   ├── GroupListDto.java
    │   │               │   │   ├── GroupListResponse.java
    │   │               │   │   ├── GroupProfileResponse.java
    │   │               │   │   ├── GroupPuzzleDto.java
    │   │               │   │   ├── GroupPuzzleListResponse.java
    │   │               │   │   └── JoinGroupRequest.java
    │   │               │   ├── presentation
    │   │               │   │   └── GroupController.java
    │   │               │   └── repository
    │   │               │       └── GroupRepository.java
    │   │               ├── puzzle
    │   │               │   ├── application
    │   │               │   │   └── PuzzleService.java
    │   │               │   ├── domain
    │   │               │   │   ├── Puzzle.java
    │   │               │   │   ├── PuzzleLocation.java
    │   │               │   │   ├── PuzzleMember.java
    │   │               │   │   └── PuzzlePiece.java
    │   │               │   ├── dto
    │   │               │   │   ├── CompletePuzzleRequest.java
    │   │               │   │   ├── CompletePuzzleResponse.java
    │   │               │   │   ├── CreatePuzzleRequest.java
    │   │               │   │   ├── CreatePuzzleResponse.java
    │   │               │   │   ├── EditPuzzleRequest.java
    │   │               │   │   ├── KakaoApiResponse.java
    │   │               │   │   ├── MyPuzzleDto.java
    │   │               │   │   ├── MyPuzzleListResponse.java
    │   │               │   │   ├── PuzzleDetailResponse.java
    │   │               │   │   ├── PuzzleEditViewResponse.java
    │   │               │   │   ├── PuzzlePieceDto.java
    │   │               │   │   ├── PuzzlePieceRequest.java
    │   │               │   │   ├── PuzzlerDto.java
    │   │               │   │   └── PuzzlerListResponse.java
    │   │               │   ├── presentation
    │   │               │   │   └── PuzzleController.java
    │   │               │   └── repository
    │   │               │       ├── PuzzleMemberRepository.java
    │   │               │       ├── PuzzlePieceRepository.java
    │   │               │       └── PuzzleRepository.java
    │   │               └── user
    │   │                   ├── application
    │   │                   │   ├── AuthService.java
    │   │                   │   ├── RedisService.java
    │   │                   │   └── UserService.java
    │   │                   ├── domain
    │   │                   │   ├── User.java
    │   │                   │   ├── UserProfile.java
    │   │                   │   └── UserTeam.java
    │   │                   ├── dto
    │   │                   │   ├── JwtDto.java
    │   │                   │   ├── LoginIdRequest.java
    │   │                   │   ├── LoginRequest.java
    │   │                   │   ├── ModifyNicknameRequest.java
    │   │                   │   ├── ModifyPasswordRequest.java
    │   │                   │   ├── MyPageResponse.java
    │   │                   │   ├── NicknameRequest.java
    │   │                   │   ├── ReissueTokenRequest.java
    │   │                   │   ├── SignoutRequest.java
    │   │                   │   ├── SignupRequest.java
    │   │                   │   └── TokenResponse.java
    │   │                   ├── presentation
    │   │                   │   └── UserController.java
    │   │                   └── repository
    │   │                       ├── UserRepository.java
    │   │                       └── UserTeamRepository.java
    │   └── resources
    │       ├── application.yml
    │       ├── static
    │       └── templates
    └── test
        └── java
            └── com
                └── lesso
                    └── neverland
                        └── NeverlandApplicationTests.java
```

</div>
</details>

## Tech Stacks
#### Backend
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/spring security-6DB33F?style=for-the-badge&logo=spring security&logoColor=white"> <img src="https://img.shields.io/badge/spring data jpa-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> ![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)  <img src="https://img.shields.io/badge/hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white"> <img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white"> 

#### Database
<img src="https://img.shields.io/badge/aws rds-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white"> <img src="https://img.shields.io/badge/aws s3-569A31?style=for-the-badge&logo=amazons3&logoColor=white"> <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/redis-DC382D?style=for-the-badge&logo=redis&logoColor=white">

#### Cloud
<img src="https://img.shields.io/badge/AWS ec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white">

#### CD
<img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"> <img src="https://img.shields.io/badge/github actions-2088FF?style=for-the-badge&logo=github actions&logoColor=white">

#### External API
<img src="https://img.shields.io/badge/ChatGPT 3.5-74AA9C?style=for-the-badge&logo=openai&logoColor=white"><img src="https://img.shields.io/badge/kakaomap api-FFCD00?style=for-the-badge&logo=kakao&logoColor=black">



#### Develop tools
<img src="https://img.shields.io/badge/intelliJ-000000?style=for-the-badge&logo=intellij idea&logoColor=white"> <img src="https://img.shields.io/badge/postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white"> <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"> <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
<br>
<br>

## Contributor
|김중현|
|:---:|
|<img src="https://github.com/JoongHyun-Kim.png" width="180" height="180" >|
| **Backend Developer** |
