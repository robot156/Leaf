# 🌿 Leaf

> 책을 읽다 마음에 남은 문장을, 나뭇잎처럼 한 장씩 모아두는 독서 기록 앱

Leaf는 읽고 있는 책을 검색해 등록하고 독서 노트를 만드는 앱입니다.
100% Kotlin · Compose Multiplatform 으로 작성된 멀티 모듈 프로젝트입니다.

Android · iOS 두 플랫폼에서 동작합니다. 화면·상태·데이터 계층 전부가 공용 코드입니다.
마이그레이션 과정과 플랫폼별 제약은 [docs/CMP_MIGRATION.md](docs/CMP_MIGRATION.md) 에 정리해 두었습니다.

## 실행

**Android**
```
./gradlew :app:installDebug
```

**iOS** — Xcode 로 `iosApp/iosApp.xcodeproj` 를 열고 실행합니다.
빌드 시 `Compile Kotlin Framework` 스크립트 단계가 `:app-ios` 프레임워크와
라이선스 JSON을 먼저 만듭니다. 커맨드라인으로 빌드하려면:

```
xcodebuild -project iosApp/iosApp.xcodeproj -scheme iosApp \
  -configuration Debug -sdk iphonesimulator \
  -destination 'platform=iOS Simulator,name=iPhone 17 Pro' build
```


## 앱 소개
<img src="docs/leaf_screenshots_row.png" width="2642"/>                                                                                           

## 모듈 구조

`app` 을 뺀 모든 모듈이 KMP 모듈입니다 (`androidTarget` + `iosArm64` + `iosSimulatorArm64`).
소스는 `src/commonMain/kotlin` 에 있고, 플랫폼 API 가 필요한 곳만 `src/androidMain` · `src/iosMain` 으로 갈라집니다.

```
Leaf
├── app                      # Android Application · DI Graph (Android 전용 진입점)
├── build-logic/convention   # Gradle Plugin
├── config                   # Detekt
├── core
│   ├── common               # Model · EventBus · Util · 플랫폼 로그/디스패처
│   ├── designsystem         # LeafTheme · Leaf Component · Modifier · compose-resources
│   ├── ui                   # MVIViewModel · Navigator · Paging · Animation
│   ├── data
│   │   ├── api              #   Repository
│   │   └── impl             #   └ RepositoryImpl · Environment
│   ├── data-local
│   │   ├── api              #   LocalData
│   │   └── impl             #   └ Room(Entity · Dao) · DataStore
│   └── data-remote
│       ├── api              #   RemoteDataSource
│       └── impl             #   └ Ktor · DTO
└── feature
    ├── main                 # MainNavHost · PlatformActions (MainActivity 는 androidMain)
    ├── intro                # 스플래시 / 최초 진입
    ├── home                 # 기록 목록
    ├── write                # 책 검색 · 노트 작성
    ├── note-detail          # 기록 상세 · 이미지 공유
    ├── setting              # 설정
    ├── setting-theme        # 설정 테마 · 팔레트
    ├── setting-license      # 설정 오픈소스 라이선스 목록/상세
    └── image-viewer         # 이미지 뷰어
```

### 리소스

- 문자열 · 드로어블 · 폰트는 `core/designsystem/src/commonMain/composeResources` 한 곳에 있고
  `Res.string.*` / `Res.drawable.*` 로 참조합니다.
- `core/designsystem/src/androidMain/res` 에는 **Android 플랫폼 리소스만** 둡니다
  (스플래시 테마 `Theme.Leaf.Splash`). compose-resources 는 Compose 코드에서만 읽히므로
  매니페스트가 `@style/` 로 참조하는 것은 여기 있어야 합니다.

### 새 feature 모듈 만들기

```
./gradlew generateFeatureModule -PfeatureName=my-feature
```

생성 후 `feature/main` 의 `MainNavHost` 에서 entry 를 등록하고,
**`LeafNavConfiguration` 에 새 Route 도 등록해야 합니다.**
(빠뜨리면 컴파일은 통과하고 백스택 복원 시점에만 실패합니다 — `feature:main` 의 테스트가 잡아줍니다)