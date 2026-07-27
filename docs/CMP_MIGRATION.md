# Leaf — Compose Multiplatform 마이그레이션 계획

> 대상 플랫폼: **Android + iOS**
> 방식: **bottom-up 단계적 전환** (각 단계마다 Android 빌드 통과 유지)
> Android 전용 기능: **expect/actual 추상화** (기능 손실 없음)

---

## 1. 의존성 KMP 호환성 검증 결과

Maven Central / Google Maven 메타데이터를 직접 조회해 iOS 타깃 퍼블리시 여부를 확인했다.
(추측이 아니라 `.module` 파일의 `org.jetbrains.kotlin.native.target` 기준)

### 그대로 쓸 수 있는 것 (좌표 변경 불필요)

| 의존성 | 현재 버전 | iOS 타깃 |
|---|---|---|
| `org.orbit-mvi:orbit-core / -viewmodel / -compose` | 12.0.0 | ✅ arm64, simulatorArm64, x64 |
| `dev.zacsweers.metro:metrox-viewmodel / -compose` | 1.3.2 | ✅ |
| `io.coil-kt.coil3:coil-compose / -network-ktor3` | 3.5.0 | ✅ |
| `net.engawapg.lib:zoomable` | 2.13.0 | ✅ |
| `com.mikepenz:aboutlibraries-core` | 15.0.4 | ✅ |
| `org.jetbrains.kotlinx:*` (datetime, serialization, coroutines, immutable) | — | ✅ |
| `androidx.room:room-runtime` | 2.8.4 | ✅ (Room KMP) |
| `androidx.datastore:datastore-preferences(-core)` | 1.2.1 | ✅ |
| `androidx.annotation:annotation` | — | ✅ |
| `io.ktor:ktor-client-*` | 3.5.1 | ✅ (`ktor-client-darwin` 추가 필요) |
| `androidx.lifecycle:*` (runtime-compose, viewmodel, viewmodel-compose, viewmodel-savedstate, viewmodel-navigation3) | 2.11.0 | ✅ **전부 iOS 퍼블리시 — 좌표 변경 불필요** |
| `androidx.navigation3:navigation3-runtime` | 1.1.4 | ✅ (runtime 은 upstream KMP) |
| `androidx.compose.runtime:runtime` | — | ✅ (upstream KMP) |
| `androidx.savedstate:savedstate-compose` | 1.4.0 | ✅ |

### 좌표를 갈아타야 하는 것

Google이 퍼블리시하는 `androidx.compose.ui / foundation / material3` 는 **iOS 타깃이 없다**
(`android` + `jvmStubs` + `linuxx64Stubs` 뿐). `androidx.compose.runtime` 만 upstream KMP.
따라서 UI 계층은 JetBrains 포크를 써야 한다.

규칙: **upstream `androidx.*` 가 iOS 를 퍼블리시하면 그대로 쓰고, 없을 때만 JetBrains 포크로 갈아탄다.**
갈아타야 하는 건 결국 **Compose UI 계층 + navigation3-ui** 뿐이다.

| 현재 | 전환 후 |
|---|---|
| `androidx.compose:compose-bom` (BOM) | BOM 없음 → 아티팩트별 버전 명시 |
| `androidx.compose.ui:ui`, `:ui-tooling-preview` | `org.jetbrains.compose.ui:ui`, `:ui-tooling-preview` |
| `androidx.compose.runtime:runtime` | `org.jetbrains.compose.runtime:runtime` (relocation 아티팩트 — Android 변형은 내부적으로 upstream 을 가리킴) |
| `androidx.compose.foundation:foundation` | `org.jetbrains.compose.foundation:foundation` |
| `androidx.compose.animation:animation` | `org.jetbrains.compose.animation:animation` |
| `androidx.compose.material3:material3` | `org.jetbrains.compose.material3:material3` ⚠️ **별도 버전 라인** |
| `androidx.navigation3:navigation3-ui` | `org.jetbrains.androidx.navigation3:navigation3-ui` |
| `androidx.compose.ui.tooling.preview.Preview` | `org.jetbrains.compose.ui.tooling.preview.Preview` |
| `R.drawable` / `R.string` / `res/font` | `compose-resources` (`Res.drawable.*`, `Res.string.*`) |
| *`androidx.lifecycle:*`, `androidx.navigation3:navigation3-runtime`* | *변경 없음* |

> `androidx.navigation3:navigation3-ui:1.1.4` 는 iOS 미퍼블리시(`android` + `jvmStubs` + `linuxx64Stubs`)임을 확인.
> 반면 `navigation3-runtime` 은 iOS 가 있고, JetBrains 는 `navigation3-ui` 만 포크한다
> (`org/jetbrains/androidx/navigation3/` 디렉터리에 `navigation3-ui` 만 존재).
> Compose 자체와 동일한 **runtime=upstream / UI=포크** 구조.

### 버전 세트 (CMP 1.11.1 stable 기준)

CMP 는 아티팩트 그룹별로 버전 라인이 갈라져 있어 릴리스 노트의 조합을 그대로 따라야 한다.

```
composeMultiplatform      = 1.11.1          # org.jetbrains.compose.{runtime,ui,foundation,animation,components}
composeMaterial3          = 1.11.0-alpha07  # org.jetbrains.compose.material3   ← 버전 라인 다름
jbNavigation3             = 1.1.1           # org.jetbrains.androidx.navigation3:navigation3-ui
androidxSqlite            = 2.7.0           # androidx.sqlite:sqlite-bundled    ← room 과 버전 라인 다름
```

> `jb navigation3-ui:1.1.1` 의 pom 은 `androidx.navigation3:navigation3-runtime:1.1.1` 을 참조하지만
> 프로젝트는 `1.1.4` 를 쓴다. 같은 1.1.x 라인이라 Gradle 이 1.1.4 로 올려 해소한다 — 빌드로 실측 확인 필요.

대안: CMP `1.12.0-beta02` (Kotlin 2.4 정렬, material3 `1.12.0-alpha04`,
lifecycle `2.11.0-rc02`, navigation3 `1.2.0-alpha03`).
현재 프로젝트가 Kotlin 2.4.10 / AGP 9.2.1 로 최신을 따르고 있어,
**1.11.1 로 시작해서 Compose 컴파일러 비호환이 나오면 1.12.0-beta02 로 올린다.**

CMP Gradle 플러그인은 KGP >= 2.0 만 요구하고 별도 Kotlin 버전 게이트는 없음(플러그인 jar 확인).

---

## 2. Android 전용 코드 인벤토리

전체 195개 `.kt` 중 Android 전용 API를 쓰는 파일은 **11개**뿐. 이 부분만 expect/actual 처리한다.

| 파일 | Android API | 전환 방침 |
|---|---|---|
| `core/data/impl/.../env/AndroidEnvironment.kt` | `Build.VERSION`, `BuildConfig` | `expect class PlatformEnvironment` + 빌드 시 생성되는 `LeafBuildConfig` |
| `core/data-local/impl/.../datastore/PreferenceStorageImpl.kt` | `preferencesDataStore(Context)` | `expect fun createDataStore()` — okio path 기반 |
| `core/data-local/impl/.../image/ImageCacheDataSourceImpl.kt` | `SingletonImageLoader.get(Context)` | Coil3 는 KMP — `PlatformContext` 로 교체 |
| `core/data-local/impl/.../license/LicenseDataSourceImpl.kt` | assets 읽기 | compose-resources 또는 `expect fun readLicenseJson()` |
| `core/data-local/impl/.../database/LeafDatabase.kt` | `Room.databaseBuilder(Context)` | `expect fun databaseBuilder()` + `sqlite-bundled` |
| `core/data-remote/impl/.../util/HtmlText.kt` | `android.text.Html` | `expect fun unescapeHtml()` — iOS 는 순수 Kotlin 구현 |
| `core/ui/.../navigation/NavTransitions.kt` | `AccelerateDecelerateInterpolator` | Compose `Easing` 으로 대체 (expect 불필요) |
| `core/ui/.../animation/MaskBox.kt` | `ValueAnimator`, `android.graphics.*` | Compose `Animatable` + `DrawScope` 로 대체 |
| `feature/note-detail` 이미지 공유 | `FileProvider`, `ShareCompat`, `Bitmap` | `expect fun shareImage()` — iOS 는 `UIActivityViewController` |
| `feature/main/MainActivity.kt` | `ComponentActivity`, `enableEdgeToEdge`, splashscreen | androidApp 에만 존치 + iOS `ComposeUIViewController` 진입점 신설 |
| `app/LeafApplication.kt`, `di/AndroidBindings.kt` | `Application`, Crashlytics | androidApp 에만 존치, Crashlytics 는 `expect object CrashReporter` |

리소스: `R.*` 참조 약 100개. drawable/font 는 `core/designsystem` 에 집중되어 있고
`LeafRes` 가 이미 간접 지점 역할을 하므로 `DrawableResource` 타입으로 바꾸면 사용처는 대부분 무변경.

---

## 3. 진행 순서 (bottom-up)

각 단계 종료 시 `./gradlew assembleDebug` 통과 + 커밋.

| # | 단계 | 내용 |
|---|---|---|
| 0 | 빌드 인프라 | `leaf.kmp.library` / `.compose` / `.feature` / `.application` convention plugin 신설, `configureKotlinMultiplatform`, `LeafBuildConfig` 생성 태스크, `libs.versions.toml` 좌표 추가 |
| 1 | `core:common` | KMP 전환 (순수 Kotlin 계층) |
| 2 | `core:data-remote` api/impl | Ktor darwin 엔진, `HtmlText` expect/actual |
| 3 | `core:data-local` api/impl | Room KMP + `sqlite-bundled`, DataStore okio, license/image expect/actual |
| 4 | `core:data` api/impl | `Environment` expect/actual + `LeafBuildConfig` |
| 5 | `core:designsystem` | compose-resources 전환, material3·Preview 좌표 교체 |
| 6 | `core:ui` | MVIViewModel / Navigator / NavTransitions / MaskBox |
| 7 | `feature/*` (9개) | intro → home → write → note-detail → setting → setting-theme → setting-license → image-viewer → main |
| 8 | 진입점 | `androidApp`(기존 app) + `iosApp` Xcode 프로젝트, `ComposeUIViewController` |
| 9 | 정리 | detekt KMP 소스셋, `templates/feature-module` 갱신, README 모듈 구조 갱신 |

---

## 4. 알려진 리스크

- **Room KMP**: `androidx.sqlite:sqlite-bundled` 는 room 과 버전 라인이 다름(현재 최신 `2.7.0`). KSP2 + KMP 소스셋 설정 필요.
- **Metro DI**: `metrox-android` 는 Android 전용 → androidMain 으로 격리. 그래프 정의는 commonMain 유지.
- **material3 버전 라인 분리**: `compose.material3` DSL 이 플러그인 기본값(`1.9.0`)을 쓰므로 DSL 대신 좌표를 직접 명시한다.
- **`@Preview`**: CMP 의 preview 는 Android Studio 에서만 렌더 가능. `PreviewParameterProvider` 도 좌표 이동.
- **Firebase Crashlytics**: iOS 대응 시 별도 SDK 필요. 1차로는 `expect object CrashReporter` 의 iosMain 을 no-op 으로 두고 후속 과제로 분리.
- **`compileSdk 37` / AGP 9.2.1**: KMP + AGP 9 조합에서 `androidLibrary` DSL 변화 가능성 — 0단계에서 실측 확인.
