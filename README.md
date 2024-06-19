# 🧚🏻 Neverland Server
> Server Repository for Capstone Design Project "Neverland

<div align="center">
  <img src="https://github.com/lemonssoju/.github/assets/80838501/84a0c925-0bc7-4dc7-b3ed-75ef5160a919" />
</div>

## 📱 About the Project
> Automatically Record and Preserve Group Memories

**Neverland** is a service that provides an efficient way to record and archive the memories of groups and organizations.

#### Automatically Record and Preserve Group Memories

Neverland allows users to automatically compile and generate a unified record of the group's memories, even when individuals remember them differently. Leveraging generative AI technology, this feature enables groups to conveniently and efficiently document their shared experiences.

#### Effective Memory Archiving
Neverland systematically organizes and stores the group's memory records from both temporal and spatial perspectives.
This enhances the diversity and efficiency of the memory archiving process.
<br>
<br>

## 🚀 Getting Started

## 📌 System Architecture
![architecture](https://github.com/lemonssoju/.github/assets/80838501/f9e6a5f3-fcbe-442b-9314-55874d10bdf4)

<br>

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

<br>

## 🛠️ Tech Stacks

### Frontend
#### Frontend
<img src="https://img.shields.io/badge/TypeScript-007ACC?style=for-the-badge&logo=typescript&logoColor=white"> <img src="https://img.shields.io/badge/React_Native-61DAFB?style=for-the-badge&logo=react&logoColor=20232A"> <img src="https://img.shields.io/badge/Recoil-3578E5?style=for-the-badge&logo=Recoil&logoColor=white"> <img src="https://img.shields.io/badge/styled--components-DB7093?style=for-the-badge&logo=styled-components&logoColor=white">

#### Platform
<img src="https://img.shields.io/badge/iOS-000000?style=for-the-badge&logo=ios&logoColor=white"> <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white">

#### External API
<img src="https://img.shields.io/badge/Stable Diffusion-B000B4?style=for-the-badge"> 

#### Develop tools
<img src="https://img.shields.io/badge/Visual_Studio_Code-0078D4?style=for-the-badge&logo=visual%20studio%20code&logoColor=white"> <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"> <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
<br>
<br>

### Backend
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

## ✨ Features
![7](https://github.com/lemonssoju/.github/assets/80838501/7e986297-6a28-4f73-8247-b03c3e8021a3)

![8](https://github.com/lemonssoju/.github/assets/80838501/10fe300f-91d2-4988-bd7a-e0b2de51541d)

![9](https://github.com/lemonssoju/.github/assets/80838501/cdde5921-0e03-4e58-872e-a0a3f45d5952)

![10](https://github.com/lemonssoju/.github/assets/80838501/89c33752-5683-453b-b536-afec175f8ec5)

![11](https://github.com/lemonssoju/.github/assets/80838501/4375aa65-6be5-4cf4-ae2f-81db03f78074)

![12](https://github.com/lemonssoju/.github/assets/80838501/092499f4-58ea-4478-9322-7db0beb15d22)

![13](https://github.com/lemonssoju/.github/assets/80838501/21b9c5d4-685f-4fb3-b235-8641bf3f222d)

<br>

### ✨ Core Feature Logic
The core logic of Neverland, the image auto-generation, is powered by **Chat-GPT** and **Stable Diffusion**, and it operates through the following workflow.
![Core Feature](https://github.com/lemonssoju/.github/assets/80838501/27df34c4-36e2-48ea-b1ad-640a69450cca)

<br>
<br>


## 🧩 Contributors
|Joonghyun Kim|Seojin Kwak|Somin Ji|
|:---:|:---:|:---:|
|<img src="https://github.com/JoongHyun-Kim.png" width="180" height="180" >|<img src="https://github.com/SJ-Kwak.png" width="180" height="180" >|<img src="https://github.com/ji-somnie.png" width="180" height="180" >|
| **Backend Developer** | **Frontend Developer**| **AI Developer** |

<br>
<br>

## 🔗 Open Source Libraries and SDK
[Stability AI's Stable Diffusion](https://platform.stability.ai/docs/api-reference#tag/SDXL-and-SD1.6) <br>
[Kakao Map API](https://developers.kakao.com/docs/latest/ko/local/dev-guide) <br>
[React Native Maps](https://github.com/react-native-maps/react-native-maps) <br>
[Chat-GPT 3.5 Java Client](https://github.com/TheoKanning/openai-java)
