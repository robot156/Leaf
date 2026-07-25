# 🌿 Leaf

> 책을 읽다 마음에 남은 문장을, 나뭇잎처럼 한 장씩 모아두는 독서 기록 앱

Leaf는 읽고 있는 책을 검색해 등록하고 독서 노트를 만드는 안드로이드 앱입니다.
100% Kotlin · Jetpack Compose 로 작성된 멀티 모듈 프로젝트입니다.


## 앱 소개
<img src="docs/leaf_screenshots_row.png" width="2642"/>                                                                                           

## 모듈 구조
```
Leaf
├── app                      # Application, DI Graph
├── build-logic/convention   # Gradle Plugin
├── config                   # Detekt
├── core
│   ├── common               # Model · EventBus · Util
│   ├── designsystem         # LeafTheme · Leaf Component · Modifier
│   ├── ui                   # MVIViewModel · Navigator · Paging · Animation
│   ├── data                 
│   │   ├── api              #   Repository
│   │   └── impl             #   └ RepositoryImpl
│   ├── data-local           
│   │   ├── api              #   LocalData
│   │   └── impl             #   └ Room(Entity · Dao) · DataStore
│   └── data-remote          
│       ├── api              #   RemoteDataSource
│       └── impl             #   └ Ktor · DTO
└── feature
    ├── main                 # MainActivity · MainNavHost
    ├── intro                # 스플래시 / 최초 진입
    ├── home                 # 기록 목록
    ├── write                # 책 검색 · 노트 작성
    ├── note-detail          # 기록 상세 · 이미지 공유
    ├── setting              # 설정
    ├── setting-theme        # 설정 테마 · 팔레트
    ├── setting-license      # 설정 오픈소스 라이선스 목록/상세
    └── image-viewer         # 이미지 뷰어
```